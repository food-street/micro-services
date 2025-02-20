package com.imthath.food_street.restaurant_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Description is required")
        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @Size(max = 500, message = "Image URL must not exceed 500 characters")
        String imageUrl,

        Long courtId
) { } 