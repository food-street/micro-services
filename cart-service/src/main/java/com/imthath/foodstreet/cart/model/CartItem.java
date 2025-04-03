package com.imthath.foodstreet.cart.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.io.Serializable;

@Data
public class CartItem implements Serializable {
    @NotBlank(message = "Menu item ID is required")
    private String menuItemId;
    
    @NotBlank(message = "Restaurant ID is required")
    private String restaurantId;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private double price;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    
    private String notes;
}