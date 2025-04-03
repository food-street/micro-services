package com.imthath.foodstreet.cart.model;

import lombok.Data;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Cart implements Serializable {
    private String userId;
    private String foodCourtId;
    private List<CartItem> items = new ArrayList<>();
    private Instant lastModified;
    private double total;
    
    public void addItem(CartItem item) {
        Optional<CartItem> existingItem = items.stream()
                .filter(i -> i.getMenuItemId().equals(item.getMenuItemId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }
        updateTotal();
    }
    
    public void removeItem(String menuItemId) {
        items.removeIf(item -> item.getMenuItemId().equals(menuItemId));
        updateTotal();
    }
    
    public void updateItemQuantity(String menuItemId, int quantity) {
        items.stream()
                .filter(i -> i.getMenuItemId().equals(menuItemId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
        if (quantity == 0) {
            items.removeIf(item -> item.getMenuItemId().equals(menuItemId));
        }
        updateTotal();
    }
    
    public boolean hasItem(String menuItemId) {
        return items.stream().anyMatch(item -> item.getMenuItemId().equals(menuItemId));
    }
    
    private void updateTotal() {
        this.total = items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
        this.lastModified = Instant.now();
    }
}