package com.imthath.foodstreet.payment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response object for payment initiation")
public class PaymentInitiationResponse {
    @Schema(description = "Unique identifier of the payment", example = "pay_123456789")
    private String paymentId;
    
    @Schema(description = "URL to complete the payment", example = "https://checkout.razorpay.com/pay/123456789")
    private String paymentUrl;
    
    @Schema(description = "ID of the order associated with the payment", example = "order_123456789")
    private String orderId;
    
    @Schema(description = "Current status of the payment", example = "PENDING")
    private String status;
    
    @Schema(description = "Payment ID from the payment gateway", example = "pay_razorpay_123456789")
    private String gatewayPaymentId;
    
    @Schema(description = "Error message if payment initiation failed", example = "Invalid payment method")
    private String errorMessage;
} 