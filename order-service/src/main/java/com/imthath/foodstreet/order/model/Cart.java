package com.imthath.foodstreet.order.model;

public record Cart(
        String userId,
        String foodCourtId,
        CartItem[] items,
        String lastModified,
        double total
) {
    public boolean isEmpty() {
        return items == null || items.length == 0;
    }
}
