package com.imthath.foodstreet.payment.gateway;

import com.imthath.foodstreet.payment.model.PaymentInitiationResponse;
import com.imthath.foodstreet.payment.model.PaymentRequest;
import com.imthath.foodstreet.payment.model.PaymentStatus;
import com.imthath.foodstreet.payment.model.RefundResponse;

import java.math.BigDecimal;

public interface PaymentGateway {
    /**
     * Initiates a payment request with the payment gateway
     * @param request Payment request details
     * @return Response containing payment URL and other details
     */
    PaymentInitiationResponse initiatePayment(PaymentRequest request);

    /**
     * Checks the current status of a payment
     * @param paymentId The payment ID to check
     * @return Current payment status
     */
    PaymentStatus checkPaymentStatus(String paymentId);

    /**
     * Processes a refund for a payment
     * @param paymentId The payment ID to refund
     * @param amount The amount to refund
     * @return Refund response with details
     */
    RefundResponse processRefund(String paymentId, BigDecimal amount);

    /**
     * Returns the name of the payment gateway
     * @return Gateway name
     */
    String getGatewayName();
} 