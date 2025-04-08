package com.imthath.foodstreet.payment.service;

import com.imthath.foodstreet.payment.config.RazorpayConfig;
import com.imthath.foodstreet.payment.model.Payment;
import com.imthath.foodstreet.payment.model.PaymentAttempt;
import com.imthath.foodstreet.payment.model.PaymentStatus;
import com.imthath.foodstreet.payment.repository.PaymentRepository;
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
        // Verify webhook signature
        if (!verifyWebhookSignature(payload, signature)) {
            throw new IllegalArgumentException("Invalid webhook signature");
        }

        JSONObject webhookData = new JSONObject(payload);
        String event = webhookData.getString("event");
        JSONObject payloadData = webhookData.getJSONObject("payload");

        switch (event) {
            case "payment.captured" -> handlePaymentCaptured(payloadData);
            case "payment.failed" -> handlePaymentFailed(payloadData);
            case "payment.refunded" -> handlePaymentRefunded(payloadData);
            default -> log.info("Unhandled webhook event: {}", event);
        }
    }

    private void handlePaymentCaptured(JSONObject payload) {
        String paymentId = payload.getJSONObject("payment").getString("order_id");
        Payment payment = paymentRepository.findByOrderId(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        payment.setStatus(PaymentStatus.CAPTURED);
        paymentRepository.save(payment);

        log.info("Payment captured for order: {}", paymentId);
    }

    private void handlePaymentFailed(JSONObject payload) {
        String paymentId = payload.getJSONObject("payment").getString("order_id");
        Payment payment = paymentRepository.findByOrderId(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        payment.setStatus(PaymentStatus.FAILED);
        
        // Create new payment attempt
        PaymentAttempt attempt = new PaymentAttempt();
        attempt.setId(UUID.randomUUID().toString());
        attempt.setPayment(payment);
        attempt.setAttemptNumber(payment.getAttempts().size() + 1);
        attempt.setStatus(PaymentStatus.FAILED);
        attempt.setErrorMessage(payload.getJSONObject("error").getString("description"));

        payment.addAttempt(attempt);
        paymentRepository.save(payment);

        log.info("Payment failed for order: {}", paymentId);
    }

    private void handlePaymentRefunded(JSONObject payload) {
        String paymentId = payload.getJSONObject("payment").getString("order_id");
        Payment payment = paymentRepository.findByOrderId(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        log.info("Payment refunded for order: {}", paymentId);
    }

    private boolean verifyWebhookSignature(String payload, String signature) {
        // TODO: Implement webhook signature verification
        // This should use the webhook secret from RazorpayConfig
        return true;
    }
} 