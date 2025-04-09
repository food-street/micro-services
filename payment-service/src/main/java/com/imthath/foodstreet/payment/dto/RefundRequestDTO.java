package com.imthath.foodstreet.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request object for processing a refund")
public class RefundRequestDTO {
    @Schema(description = "ID of the payment to refund", example = "pay_123456789", required = true)
    @NotBlank(message = "Payment ID is required")
    private String paymentId;

    @Schema(description = "Amount to refund", example = "99.99", required = true)
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
} 