package com.imthath.food_street.court_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourtRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Location is required")
        @Size(max = 500, message = "Location must not exceed 500 characters")
        String location,

        @Size(max = 500, message = "Image URL must not exceed 500 characters")
        String imageUrl
) { }
