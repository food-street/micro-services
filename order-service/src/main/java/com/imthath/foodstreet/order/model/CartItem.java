package com.imthath.foodstreet.order.model;

public record CartItem(
        String menuItemId,
        String restaurantId,
        String name,
        double price,
        int quantity,
        String notes
) {
    public double getTotalPrice() {
        return price * quantity;
    }
}