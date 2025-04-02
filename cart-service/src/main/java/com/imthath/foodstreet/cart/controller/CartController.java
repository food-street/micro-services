package com.imthath.foodstreet.cart.controller;

import com.imthath.foodstreet.cart.model.Cart;
import com.imthath.foodstreet.cart.model.CartItem;
import com.imthath.foodstreet.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<Cart> addItem(
            @PathVariable String userId,
            @RequestParam String foodCourtId,
            @RequestBody CartItem item) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, foodCourtId, item));
    }

    @PutMapping("/{userId}/items/{menuItemId}")
    public ResponseEntity<Cart> updateItemQuantity(
            @PathVariable String userId,
            @PathVariable String menuItemId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, menuItemId, quantity));
    }

    @DeleteMapping("/{userId}/items/{menuItemId}")
    public ResponseEntity<Cart> removeItem(
            @PathVariable String userId,
            @PathVariable String menuItemId) {
        return ResponseEntity.ok(cartService.removeItem(userId, menuItemId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}