package com.imthath.food_street.menu_service.dto;

import com.imthath.food_street.menu_service.model.Category;
import com.imthath.food_street.menu_service.model.MenuItem;

import java.util.Date;
import java.util.List;

public record CategoryResponse(
    String id,
    String name,
    String restaurantId,
    String description,
    int displayOrder,
    boolean isAvailable,
    Date createdAt,
    Date updatedAt,
    List<MenuItem> items
) {
    public static CategoryResponse from(Category category, List<MenuItem> items) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getRestaurantId(),
            category.getDescription(),
            category.getDisplayOrder(),
            category.isAvailable(),
            category.getCreatedAt(),
            category.getUpdatedAt(),
            items
        );
    }
}
