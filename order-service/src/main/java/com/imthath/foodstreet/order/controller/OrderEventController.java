package com.imthath.foodstreet.order.controller;

import com.imthath.foodstreet.order.service.OrderEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderEventController {
    
    private final OrderEventService orderEventService;
    
    @GetMapping(value = "/{orderId}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToOrderEvents(@PathVariable String orderId) {
        log.info("New SSE subscription request for order: {}", orderId);
        SseEmitter emitter = new SseEmitter();
        
        // Store emitter for this order
        orderEventService.addEmitter(orderId, emitter);
        
        // Cleanup on client disconnect
        emitter.onCompletion(() -> {
            log.debug("SSE connection completed for order: {}", orderId);
            orderEventService.removeEmitter(orderId, emitter);
        });
        
        emitter.onTimeout(() -> {
            log.debug("SSE connection timed out for order: {}", orderId);
            orderEventService.removeEmitter(orderId, emitter);
        });
        
        emitter.onError(e -> {
            log.error("SSE connection error for order: {}", orderId, e);
            orderEventService.removeEmitter(orderId, emitter);
        });
        
        return emitter;
    }
} 