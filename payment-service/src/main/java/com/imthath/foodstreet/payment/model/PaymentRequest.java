package com.imthath.foodstreet.payment.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRequest {
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String description;
} 