package com.imthath.foodstreet.payment.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RefundResponse {
    private String refundId;
    private String paymentId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String gatewayRefundId;
    private LocalDateTime refundedAt;
    private String errorMessage;
} 