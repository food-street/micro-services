package com.imthath.food_street.menu_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

@Component
public interface RestaurantClient {
    @GetExchange("/restaurant/check")
    boolean checkRestaurantExists(@RequestParam Long id);
}
