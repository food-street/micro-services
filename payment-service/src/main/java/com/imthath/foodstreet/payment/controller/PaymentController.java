package com.imthath.foodstreet.payment.controller;

import com.imthath.foodstreet.payment.dto.PaymentRequestDTO;
import com.imthath.foodstreet.payment.dto.RefundRequestDTO;
import com.imthath.foodstreet.payment.model.Payment;
import com.imthath.foodstreet.payment.model.PaymentInitiationResponse;
import com.imthath.foodstreet.payment.model.PaymentStatus;
import com.imthath.foodstreet.payment.model.RefundResponse;
import com.imthath.foodstreet.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentInitiationResponse> initiatePayment(
            @Valid @RequestBody PaymentRequestDTO request,
            @RequestParam(defaultValue = "RAZORPAY") String gatewayType) {
        return ResponseEntity.ok(paymentService.initiatePayment(request, gatewayType));
    }

    @GetMapping("/{paymentId}/status")
    public ResponseEntity<PaymentStatus> getPaymentStatus(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.checkPaymentStatus(paymentId));
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<RefundResponse> processRefund(
            @PathVariable String paymentId,
            @Valid @RequestBody RefundRequestDTO request) {
        return ResponseEntity.ok(paymentService.processRefund(paymentId, request.getAmount()));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
} 