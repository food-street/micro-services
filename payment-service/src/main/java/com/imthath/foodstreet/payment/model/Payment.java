package com.imthath.foodstreet.payment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "payments")
@Schema(description = "Payment entity representing a payment transaction")
public class Payment {
    @Schema(description = "Unique identifier of the payment", example = "pay_123456789")
    @Id
    private String id;

    @Schema(description = "ID of the order associated with the payment", example = "order_123456789")
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Schema(description = "Payment amount", example = "99.99")
    @Column(nullable = false)
    private BigDecimal amount;

    @Schema(description = "Currency of the payment", example = "INR")
    @Column(nullable = false)
    private String currency;

    @Schema(description = "Current status of the payment", example = "CAPTURED")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Schema(description = "Type of payment gateway used", example = "RAZORPAY")
    @Column(name = "gateway_type", nullable = false)
    private String gatewayType;

    @Schema(description = "Payment ID from the payment gateway", example = "pay_razorpay_123456789")
    @Column(name = "gateway_payment_id")
    private String gatewayPaymentId;

    @Schema(description = "List of payment attempts")
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentAttempt> attempts = new ArrayList<>();

    @Schema(description = "Timestamp when the payment was created", example = "2023-06-15T14:30:00")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the payment was last updated", example = "2023-06-15T14:35:00")
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void addAttempt(PaymentAttempt attempt) {
        attempts.add(attempt);
        attempt.setPayment(this);
    }
} 