package com.imthath.foodstreet.cart.service;

import com.imthath.foodstreet.cart.model.Cart;
import com.imthath.foodstreet.cart.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CART_KEY_PREFIX = "cart:";
    
    @Value("${cart.expiry.hours}")
    private int cartExpiryHours;
    
    private String getCartKey(String userId) {
        return CART_KEY_PREFIX + userId;
    }
    
    public Cart getCart(String userId) {
        Object cartObj = redisTemplate.opsForValue().get(getCartKey(userId));
        return cartObj != null ? (Cart) cartObj : new Cart();
    }
    
    public void saveCart(String userId, Cart cart) {
        String key = getCartKey(userId);
        redisTemplate.opsForValue().set(key, cart, Duration.ofHours(cartExpiryHours));
    }
    
    public Cart addItemToCart(String userId, String foodCourtId, CartItem item) {
        Cart cart = getCart(userId);
        if (cart.getFoodCourtId() == null) {
            cart.setFoodCourtId(foodCourtId);
            cart.setUserId(userId);
        } else if (!cart.getFoodCourtId().equals(foodCourtId)) {
            throw new IllegalStateException("Cannot add items from different food courts");
        }
        
        cart.addItem(item);
        saveCart(userId, cart);
        return cart;
    }
    
    public Cart updateItemQuantity(String userId, String menuItemId, int quantity) {
        Cart cart = getCart(userId);
        cart.updateItemQuantity(menuItemId, quantity);
        saveCart(userId, cart);
        return cart;
    }
    
    public Cart removeItem(String userId, String menuItemId) {
        Cart cart = getCart(userId);
        cart.removeItem(menuItemId);
        saveCart(userId, cart);
        return cart;
    }
    
    public void clearCart(String userId) {
        redisTemplate.delete(getCartKey(userId));
    }
}