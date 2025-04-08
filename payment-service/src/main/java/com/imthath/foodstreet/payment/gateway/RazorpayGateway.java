package com.imthath.foodstreet.payment.gateway;

import com.imthath.foodstreet.payment.config.RazorpayConfig;
import com.imthath.foodstreet.payment.model.PaymentInitiationResponse;
import com.imthath.foodstreet.payment.model.PaymentRequest;
import com.imthath.foodstreet.payment.model.PaymentStatus;
import com.imthath.foodstreet.payment.model.RefundResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RazorpayGateway implements PaymentGateway {
    private final RazorpayClient razorpayClient;
    private final RazorpayConfig config;

    @Override
    public PaymentInitiationResponse initiatePayment(PaymentRequest request) {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount().multiply(new BigDecimal("100")).intValue()); // Amount in paise
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", request.getOrderId());
            orderRequest.put("notes", new JSONObject()
                .put("order_id", request.getOrderId())
                .put("customer_name", request.getCustomerName())
                .put("customer_email", request.getCustomerEmail())
                .put("customer_phone", request.getCustomerPhone()));

            Order order = razorpayClient.orders.create(orderRequest);
            
            return PaymentInitiationResponse.builder()
                .paymentId(order.get("id"))
                .orderId(request.getOrderId())
                .status(PaymentStatus.PENDING.name())
                .gatewayPaymentId(order.get("id"))
                .build();
        } catch (RazorpayException e) {
            log.error("Failed to create Razorpay order", e);
            return PaymentInitiationResponse.builder()
                .orderId(request.getOrderId())
                .status(PaymentStatus.FAILED.name())
                .errorMessage(e.getMessage())
                .build();
        }
    }

    @Override
    public PaymentStatus checkPaymentStatus(String paymentId) {
        try {
            Order order = razorpayClient.orders.fetch(paymentId);
            return mapRazorpayStatus(order.get("status"));
        } catch (RazorpayException e) {
            log.error("Failed to fetch Razorpay order status", e);
            return PaymentStatus.FAILED;
        }
    }

    @Override
    public RefundResponse processRefund(String paymentId, BigDecimal amount) {
        try {
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", amount.multiply(new BigDecimal("100")).intValue()); // Amount in paise
            refundRequest.put("speed", "normal");

            com.razorpay.Refund refund = razorpayClient.refunds.create(refundRequest);
            
            return RefundResponse.builder()
                .refundId(refund.get("id"))
                .paymentId(paymentId)
                .amount(amount)
                .status(refund.get("status"))
                .gatewayRefundId(refund.get("id"))
                .refundedAt(LocalDateTime.now())
                .build();
        } catch (RazorpayException e) {
            log.error("Failed to process refund", e);
            return RefundResponse.builder()
                .paymentId(paymentId)
                .amount(amount)
                .status("FAILED")
                .errorMessage(e.getMessage())
                .build();
        }
    }

    @Override
    public String getGatewayName() {
        return "RAZORPAY";
    }

    private PaymentStatus mapRazorpayStatus(String razorpayStatus) {
        return switch (razorpayStatus.toUpperCase()) {
            case "CREATED" -> PaymentStatus.PENDING;
            case "PAID" -> PaymentStatus.CAPTURED;
            case "FAILED" -> PaymentStatus.FAILED;
            case "CANCELLED" -> PaymentStatus.CANCELLED;
            default -> PaymentStatus.PENDING;
        };
    }
} 