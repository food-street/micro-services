package com.imthath.foodstreet.order.client;

import com.imthath.foodstreet.order.model.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service")
public interface CartClient {
    @GetMapping("/api/v1/carts/{userId}")
    Cart getCart(@PathVariable String userId);
} 