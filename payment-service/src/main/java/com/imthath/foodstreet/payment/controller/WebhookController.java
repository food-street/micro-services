package com.imthath.foodstreet.payment.controller;

import com.imthath.foodstreet.payment.service.PaymentWebhookHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {
    private final PaymentWebhookHandler webhookHandler;

    @PostMapping("/razorpay")
    public ResponseEntity<Void> handleRazorpayWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {
        log.info("Received Razorpay webhook");
        webhookHandler.handleWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }
} 