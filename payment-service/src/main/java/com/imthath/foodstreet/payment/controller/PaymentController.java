package com.imthath.foodstreet.payment.controller;

import com.imthath.foodstreet.payment.dto.PaymentRequestDTO;
import com.imthath.foodstreet.payment.dto.RefundRequestDTO;
import com.imthath.foodstreet.payment.model.Payment;
import com.imthath.foodstreet.payment.model.PaymentInitiationResponse;
import com.imthath.foodstreet.payment.model.PaymentStatus;
import com.imthath.foodstreet.payment.model.RefundResponse;
import com.imthath.foodstreet.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment API", description = "APIs for payment processing and management")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(
        summary = "Initiate a payment",
        description = "Creates a new payment and returns payment details including a URL to complete the payment"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment initiated successfully",
            content = @Content(schema = @Schema(implementation = PaymentInitiationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/initiate")
    public ResponseEntity<PaymentInitiationResponse> initiatePayment(
            @Parameter(description = "Payment request details", required = true)
            @Valid @RequestBody PaymentRequestDTO request,
            @Parameter(description = "Payment gateway type (default: RAZORPAY)", example = "RAZORPAY")
            @RequestParam(defaultValue = "RAZORPAY") String gatewayType) {
        return ResponseEntity.ok(paymentService.initiatePayment(request, gatewayType));
    }

    @Operation(
        summary = "Get payment status",
        description = "Retrieves the current status of a payment by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment status retrieved successfully",
            content = @Content(schema = @Schema(implementation = PaymentStatus.class))),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<PaymentStatus> getPaymentStatus(
            @Parameter(description = "ID of the payment to check", required = true, example = "pay_123456789")
            @PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.checkPaymentStatus(paymentId));
    }

    @Operation(
        summary = "Process a refund",
        description = "Processes a refund for a payment"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Refund processed successfully",
            content = @Content(schema = @Schema(implementation = RefundResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid refund request"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<RefundResponse> processRefund(
            @Parameter(description = "ID of the payment to refund", required = true, example = "pay_123456789")
            @PathVariable String paymentId,
            @Parameter(description = "Refund request details", required = true)
            @Valid @RequestBody RefundRequestDTO request) {
        return ResponseEntity.ok(paymentService.processRefund(paymentId, request.getAmount()));
    }

    @Operation(
        summary = "Get payment details",
        description = "Retrieves detailed information about a payment by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully",
            content = @Content(schema = @Schema(implementation = Payment.class))),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(
            @Parameter(description = "ID of the payment to retrieve", required = true, example = "pay_123456789")
            @PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @Operation(
        summary = "Get payment by order ID",
        description = "Retrieves payment information associated with an order ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully",
            content = @Content(schema = @Schema(implementation = Payment.class))),
        @ApiResponse(responseCode = "404", description = "Payment not found for the given order ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(
            @Parameter(description = "ID of the order to find payment for", required = true, example = "order_123456789")
            @PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
} 