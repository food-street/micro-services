package com.imthath.food_street.menu_service.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateItemRequest(
    String name,
    String description,
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    BigDecimal price,
    String imageUrl,
    @Min(value = 0, message = "Display order must be non-negative")
    Integer displayOrder,
    Boolean isAvailable
) {}
