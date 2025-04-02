package com.imthath.foodstreet.cart.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class CartItem implements Serializable {
    private String menuItemId;
    private String restaurantId;
    private String name;
    private double price;
    private int quantity;
    private String notes;
}