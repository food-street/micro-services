package com.imthath.foodstreet.order.client;

import com.imthath.foodstreet.order.model.Cart;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface CartClient {
    @GetExchange("/api/v1/carts/{userId}")
    Cart getCart(@PathVariable String userId);
}