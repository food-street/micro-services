package com.imthath.foodstreet.payment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Response object for refund processing")
public class RefundResponse {
    @Schema(description = "Unique identifier of the refund", example = "ref_123456789")
    private String refundId;
    
    @Schema(description = "ID of the payment that was refunded", example = "pay_123456789")
    private String paymentId;
    
    @Schema(description = "Amount refunded", example = "99.99")
    private BigDecimal amount;
    
    @Schema(description = "Currency of the refund", example = "INR")
    private String currency;
    
    @Schema(description = "Status of the refund", example = "SUCCESS")
    private String status;
    
    @Schema(description = "Refund ID from the payment gateway", example = "ref_razorpay_123456789")
    private String gatewayRefundId;
    
    @Schema(description = "Timestamp when the refund was processed", example = "2023-06-15T14:30:00")
    private LocalDateTime refundedAt;
    
    @Schema(description = "Error message if refund processing failed", example = "Insufficient balance")
    private String errorMessage;
} 