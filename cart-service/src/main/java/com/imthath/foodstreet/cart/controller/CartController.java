package com.imthath.foodstreet.cart.controller;

import com.imthath.foodstreet.cart.model.Cart;
import com.imthath.foodstreet.cart.model.CartItem;
import com.imthath.foodstreet.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Cart getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.OK)
    public Cart addItem(
            @PathVariable String userId,
            @RequestParam String foodCourtId,
            @RequestBody CartItem item) {
        return cartService.addItemToCart(userId, foodCourtId, item);
    }

    @PutMapping("/{userId}/items/{menuItemId}")
    @ResponseStatus(HttpStatus.OK)
    public Cart updateItemQuantity(
            @PathVariable String userId,
            @PathVariable String menuItemId,
            @RequestParam int quantity) {
        return cartService.updateItemQuantity(userId, menuItemId, quantity);
    }

    @DeleteMapping("/{userId}/items/{menuItemId}")
    @ResponseStatus(HttpStatus.OK)
    public Cart removeItem(
            @PathVariable String userId,
            @PathVariable String menuItemId) {
        return cartService.removeItem(userId, menuItemId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
    }
}