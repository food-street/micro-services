package com.imthath.foodstreet.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class OrderEventService {
    private final Map<String, Set<SseEmitter>> orderEmitters = new ConcurrentHashMap<>();

    public void addEmitter(String orderId, SseEmitter emitter) {
        orderEmitters.computeIfAbsent(orderId, k -> ConcurrentHashMap.newKeySet())
                    .add(emitter);
        log.debug("Added SSE emitter for order: {}", orderId);
    }

    public void removeEmitter(String orderId, SseEmitter emitter) {
        Set<SseEmitter> emitters = orderEmitters.get(orderId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                orderEmitters.remove(orderId);
            }
            log.debug("Removed SSE emitter for order: {}", orderId);
        }
    }

    public void sendOrderUpdate(String orderId, Object update) {
        Set<SseEmitter> emitters = orderEmitters.get(orderId);
        if (emitters != null) {
            emitters.forEach(emitter -> {
                try {
                    emitter.send(update);
                } catch (IOException e) {
                    log.error("Failed to send SSE update for order: {}", orderId, e);
                    removeEmitter(orderId, emitter);
                }
            });
        }
    }
} 