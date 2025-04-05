package com.imthath.foodstreet.order.controller;

import com.imthath.foodstreet.order.model.Order;
import com.imthath.foodstreet.order.model.OrderStatus;
import com.imthath.foodstreet.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<Order> createOrder(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.createOrder(userId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @GetMapping("/food-courts/{foodCourtId}")
    public ResponseEntity<List<Order>> getOrdersByFoodCourt(@PathVariable String foodCourtId) {
        return ResponseEntity.ok(orderService.getOrdersByFoodCourt(foodCourtId));
    }

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<Order>> getOrdersByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(orderService.getOrdersByRestaurant(restaurantId));
    }
} 