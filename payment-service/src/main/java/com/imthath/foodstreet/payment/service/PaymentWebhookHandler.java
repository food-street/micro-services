package com.imthath.foodstreet.payment.service;

import com.imthath.foodstreet.payment.config.RazorpayConfig;
import com.imthath.foodstreet.payment.model.Payment;
import com.imthath.foodstreet.payment.model.PaymentAttempt;
import com.imthath.foodstreet.payment.model.PaymentStatus;
import com.imthath.foodstreet.payment.repository.PaymentRepository;
import com.razorpay.Utils;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebhookHandler {
    private final PaymentRepository paymentRepository;
    private final RazorpayConfig razorpayConfig;

    @Transactional
    public void handleWebhook(String payload, String signature) {
        log.info("Verifying webhook signature...");
        // Verify webhook signature
        if (!verifyWebhookSignature(payload, signature)) {
            log.error("Invalid webhook signature received.");
            throw new IllegalArgumentException("Invalid webhook signature");
        }
        log.info("Webhook signature verified successfully.");

        JSONObject webhookData = new JSONObject(payload);
        String event = webhookData.getString("event");
        JSONObject payloadData = webhookData.getJSONObject("payload");

        log.info("Processing webhook event: {}", event);
        switch (event) {
            case "payment.captured" -> handlePaymentCaptured(payloadData);
            case "payment.failed" -> handlePaymentFailed(payloadData);
            case "payment.refunded" -> handlePaymentRefunded(payloadData);
            default -> log.info("Unhandled webhook event: {}", event);
        }
    }

    private void handlePaymentCaptured(JSONObject payload) {
        String orderId = payload.getJSONObject("payment").getJSONObject("notes").getString("order_id");
        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found for order: " + orderId));

        payment.setStatus(PaymentStatus.CAPTURED);
        // Optionally update gateway payment ID if needed
        payment.setGatewayPaymentId(payload.getJSONObject("payment").getString("id"));
        paymentRepository.save(payment);

        log.info("Payment captured for order: {}", orderId);
    }

    private void handlePaymentFailed(JSONObject payload) {
        String orderId = payload.getJSONObject("payment").getJSONObject("notes").getString("order_id");
        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found for order: " + orderId));

        payment.setStatus(PaymentStatus.FAILED);
        
        // Create new payment attempt
        PaymentAttempt attempt = new PaymentAttempt();
        attempt.setId(UUID.randomUUID().toString());
        attempt.setPayment(payment);
        // Find the latest attempt number
        int latestAttemptNumber = payment.getAttempts().stream()
                .mapToInt(PaymentAttempt::getAttemptNumber)
                .max()
                .orElse(0);
        attempt.setAttemptNumber(latestAttemptNumber + 1);
        attempt.setStatus(PaymentStatus.FAILED);
        attempt.setGatewayPaymentId(payload.getJSONObject("payment").getString("id"));
        attempt.setErrorMessage(payload.getJSONObject("error").getString("description"));

        payment.addAttempt(attempt);
        paymentRepository.save(payment);

        log.info("Payment failed for order: {}", orderId);
    }

    private void handlePaymentRefunded(JSONObject payload) {
        // Refund webhooks might provide payment_id directly
        String gatewayPaymentId = payload.getJSONObject("refund").getString("payment_id");
        Payment payment = paymentRepository.findByGatewayPaymentId(gatewayPaymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found for gateway ID: " + gatewayPaymentId));

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        log.info("Payment refunded for order: {} (Gateway ID: {})", payment.getOrderId(), gatewayPaymentId);
    }

    private boolean verifyWebhookSignature(String payload, String signature) {
        try {
            return Utils.verifyWebhookSignature(payload, signature, razorpayConfig.getWebhookSecret());
        } catch (RazorpayException e) {
            log.error("Error verifying webhook signature", e);
            return false;
        }
    }
}