package com.imthath.foodstreet.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request object for initiating a payment")
public class PaymentRequestDTO {
    @Schema(description = "Unique identifier of the order", example = "order_123456789", required = true)
    @NotBlank(message = "Order ID is required")
    private String orderId;

    @Schema(description = "Payment amount", example = "99.99", required = true)
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @Schema(description = "Currency code (e.g., INR, USD)", example = "INR", required = true)
    @NotBlank(message = "Currency is required")
    private String currency;

    @Schema(description = "Payment method (e.g., CARD, UPI, NETBANKING)", example = "CARD", required = true)
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @Schema(description = "Name of the customer", example = "John Doe", required = true)
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Schema(description = "Email address of the customer", example = "john.doe@example.com", required = true)
    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    @Schema(description = "Phone number of the customer", example = "+919876543210")
    private String customerPhone;

    @Schema(description = "Additional description or notes for the payment", example = "Payment for Food Street order #12345")
    private String description;
} 