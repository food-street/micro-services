package com.imthath.foodstreet.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service")
public interface RestaurantClient {
    @GetMapping("/api/v1/restaurants/{restaurantId}/menu-items/{menuItemId}/availability")
    boolean checkMenuItemAvailability(@PathVariable String restaurantId, @PathVariable String menuItemId);
    
    @GetMapping("/api/v1/restaurants/{restaurantId}/food-court")
    String getRestaurantFoodCourt(@PathVariable String restaurantId);
} 