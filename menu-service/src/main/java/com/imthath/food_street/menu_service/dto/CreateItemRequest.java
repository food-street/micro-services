package com.imthath.food_street.menu_service.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateItemRequest(
    @NotNull(message = "Restaurant ID is required")
    @Min(value = 1, message = "Restaurant ID must be greater than 0")
    long restaurantId,

    @NotBlank(message = "Category ID is required")
    String categoryId,

    @NotBlank(message = "Name is required")
    String name,

    String description,

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    BigDecimal price,

    String imageUrl,

    @NotNull(message = "Display order is required")
    @Min(value = 1, message = "Display order must be greater than 0")
    int displayOrder,

    @NotNull(message = "Availability status is required")
    boolean isAvailable
) {}
