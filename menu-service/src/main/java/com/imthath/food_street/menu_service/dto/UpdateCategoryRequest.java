package com.imthath.food_street.menu_service.dto;

public record UpdateCategoryRequest(
    String name,
    String description,
    String imageUrl,
    Integer displayOrder,
    Boolean isAvailable
) {}
