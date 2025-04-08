package com.imthath.foodstreet.payment.service;

import com.imthath.foodstreet.payment.dto.PaymentRequestDTO;
import com.imthath.foodstreet.payment.gateway.PaymentGateway;
import com.imthath.foodstreet.payment.gateway.PaymentGatewayFactory;
import com.imthath.foodstreet.payment.model.Payment;
import com.imthath.foodstreet.payment.model.PaymentAttempt;
import com.imthath.foodstreet.payment.model.PaymentInitiationResponse;
import com.imthath.foodstreet.payment.model.PaymentRequest;
import com.imthath.foodstreet.payment.model.PaymentStatus;
import com.imthath.foodstreet.payment.model.RefundResponse;
import com.imthath.foodstreet.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayFactory paymentGatewayFactory;

    @Transactional
    public PaymentInitiationResponse initiatePayment(PaymentRequestDTO requestDTO, String gatewayType) {
        log.info("Initiating payment for order: {} with gateway: {}", requestDTO.getOrderId(), gatewayType);
        
        // Check if payment already exists
        paymentRepository.findByOrderId(requestDTO.getOrderId())
            .ifPresent(payment -> {
                throw new IllegalStateException("Payment already exists for order: " + requestDTO.getOrderId());
            });

        // Convert DTO to domain model
        PaymentRequest request = PaymentRequest.builder()
            .orderId(requestDTO.getOrderId())
            .amount(requestDTO.getAmount())
            .currency(requestDTO.getCurrency())
            .paymentMethod(requestDTO.getPaymentMethod())
            .customerName(requestDTO.getCustomerName())
            .customerEmail(requestDTO.getCustomerEmail())
            .customerPhone(requestDTO.getCustomerPhone())
            .description(requestDTO.getDescription())
            .build();

        // Get appropriate payment gateway
        PaymentGateway gateway = paymentGatewayFactory.getGateway(gatewayType);
        
        // Initiate payment with gateway
        PaymentInitiationResponse response = gateway.initiatePayment(request);
        
        // Create payment record
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus(PaymentStatus.valueOf(response.getStatus()));
        payment.setGatewayType(gatewayType);
        payment.setGatewayPaymentId(response.getGatewayPaymentId());

        // Create payment attempt
        PaymentAttempt attempt = new PaymentAttempt();
        attempt.setId(UUID.randomUUID().toString());
        attempt.setPayment(payment);
        attempt.setAttemptNumber(1);
        attempt.setStatus(PaymentStatus.valueOf(response.getStatus()));
        attempt.setErrorMessage(response.getErrorMessage());

        payment.addAttempt(attempt);
        paymentRepository.save(payment);

        return response;
    }

    @Transactional(readOnly = true)
    public PaymentStatus checkPaymentStatus(String paymentId) {
        log.info("Checking payment status for payment: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        PaymentGateway gateway = paymentGatewayFactory.getGateway(payment.getGatewayType());
        PaymentStatus currentStatus = gateway.checkPaymentStatus(payment.getGatewayPaymentId());

        // Update payment status if changed
        if (!currentStatus.equals(payment.getStatus())) {
            payment.setStatus(currentStatus);
            paymentRepository.save(payment);
        }

        return currentStatus;
    }

    @Transactional
    public RefundResponse processRefund(String paymentId, BigDecimal amount) {
        log.info("Processing refund for payment: {} with amount: {}", paymentId, amount);
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        if (!payment.getStatus().equals(PaymentStatus.CAPTURED)) {
            throw new IllegalStateException("Payment must be in CAPTURED status to process refund");
        }

        PaymentGateway gateway = paymentGatewayFactory.getGateway(payment.getGatewayType());
        RefundResponse response = gateway.processRefund(payment.getGatewayPaymentId(), amount);

        // Update payment status based on refund response
        if ("SUCCESS".equals(response.getStatus())) {
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }

        return response;
    }

    @Transactional(readOnly = true)
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
    }

    @Transactional(readOnly = true)
    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found for order: " + orderId));
    }
} 