package com.imthath.foodstreet.payment.repository;

import com.imthath.foodstreet.payment.model.Payment;
import com.imthath.foodstreet.payment.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByGatewayType(String gatewayType);
} 