package com.imthath.foodstreet.cart.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class Cart implements Serializable {
    private String userId;
    private String foodCourtId;
    private Map<String, CartItem> items = new HashMap<>();
    private LocalDateTime lastModified;
    private double total;
    
    public void addItem(CartItem item) {
        items.put(item.getMenuItemId(), item);
        updateTotal();
    }
    
    public void removeItem(String menuItemId) {
        items.remove(menuItemId);
        updateTotal();
    }
    
    public void updateItemQuantity(String menuItemId, int quantity) {
        CartItem item = items.get(menuItemId);
        if (item != null) {
            item.setQuantity(quantity);
            updateTotal();
        }
    }
    
    private void updateTotal() {
        this.total = items.values().stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
        this.lastModified = LocalDateTime.now();
    }
}