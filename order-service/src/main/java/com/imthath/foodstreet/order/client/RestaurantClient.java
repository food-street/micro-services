package com.imthath.foodstreet.order.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface RestaurantClient {
    @GetExchange("/api/v1/restaurants/{restaurantId}/menu-items/{menuItemId}/availability")
    boolean checkMenuItemAvailability(@PathVariable String restaurantId, @PathVariable String menuItemId);

    @GetExchange("/api/v1/restaurants/{restaurantId}/food-court")
    String getRestaurantFoodCourt(@PathVariable String restaurantId);
}