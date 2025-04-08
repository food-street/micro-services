package com.imthath.foodstreet.payment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInitiationResponse {
    private String paymentId;
    private String paymentUrl;
    private String orderId;
    private String status;
    private String gatewayPaymentId;
    private String errorMessage;
} 