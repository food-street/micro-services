package com.imthath.foodstreet.payment.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enum representing the possible states of a payment")
public enum PaymentStatus {
    @Schema(description = "Payment is pending and awaiting completion")
    PENDING,
    
    @Schema(description = "Payment has been authorized but not yet captured")
    AUTHORIZED,
    
    @Schema(description = "Payment has been successfully captured")
    CAPTURED,
    
    @Schema(description = "Payment has failed")
    FAILED,
    
    @Schema(description = "Payment has been fully refunded")
    REFUNDED,
    
    @Schema(description = "Payment has been partially refunded")
    PARTIALLY_REFUNDED,
    
    @Schema(description = "Payment has been cancelled")
    CANCELLED
} 