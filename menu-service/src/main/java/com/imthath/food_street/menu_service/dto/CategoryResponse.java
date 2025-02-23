package com.imthath.food_street.menu_service.dto;

import com.imthath.food_street.menu_service.model.Category;
import com.imthath.food_street.menu_service.model.Item;

import java.util.Date;
import java.util.List;

public record CategoryResponse(
    String id,
    String name,
    long restaurantId,
    String description,
    int displayOrder,
    Date createdAt,
    Date updatedAt,
    List<Item> items
) {
    public static CategoryResponse from(Category category, List<Item> items) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getRestaurantId(),
            category.getDescription(),
            category.getDisplayOrder(),
            category.getCreatedAt(),
            category.getUpdatedAt(),
            items
        );
    }
}
