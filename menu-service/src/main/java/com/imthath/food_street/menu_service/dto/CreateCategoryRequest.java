package com.imthath.food_street.menu_service.dto;

import jakarta.validation.constraints.*;

public record CreateCategoryRequest(
    @NotNull(message = "Restaurant ID is required")
    long restaurantId,

    @NotBlank(message = "Name is required")
    String name,

    String description,

    String imageUrl,

    @NotNull(message = "Display order is required")
    @Min(value = 0, message = "Display order must be non-negative")
    int displayOrder,

    @NotNull(message = "Availability status is required")
    boolean isAvailable
) {}
