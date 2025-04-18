package com.imthath.foodstreet.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imthath.foodstreet.cart.error.CartError;
import com.imthath.foodstreet.cart.model.Cart;
import com.imthath.foodstreet.cart.model.CartItem;
import com.imthath.utils.guardrail.GenericException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CartService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CART_KEY_PREFIX = "cart:";
    
    @Value("${cart.expiry.hours}")
    private int cartExpiryHours;
    
    private String getCartKey(String userId) {
        return CART_KEY_PREFIX + userId;
    }
    
    public Cart getCart(String userId) {
        try {
            Object cartObj = redisTemplate.opsForValue().get(getCartKey(userId));
            if (cartObj == null) {
                throw new GenericException(CartError.CART_NOT_FOUND);
            }
            return objectMapper.convertValue(cartObj, Cart.class);
        } catch (GenericException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericException(CartError.CART_READ_FAILED);
        }
    }
    
    public Cart getCartOrCreate(String userId) {
        try {
            Object cartObj = redisTemplate.opsForValue().get(getCartKey(userId));
            if (cartObj == null) {
                Cart cart = new Cart();
                cart.setUserId(userId);
                return cart;
            }
            return objectMapper.convertValue(cartObj, Cart.class);
        } catch (Exception e) {
            throw new GenericException(CartError.CART_READ_FAILED);
        }
    }
    
    public void saveCart(String userId, Cart cart) {
        try {
            String key = getCartKey(userId);
            redisTemplate.opsForValue().set(key, cart, Duration.ofHours(cartExpiryHours));
        } catch (Exception e) {
            throw new GenericException(CartError.CART_SAVE_FAILED);
        }
    }
    
    public Cart addItemToCart(String userId, String foodCourtId, CartItem item) {
        Cart cart = getCartOrCreate(userId);
        if (cart.getFoodCourtId() == null) {
            cart.setFoodCourtId(foodCourtId);
        } else if (!cart.getFoodCourtId().equals(foodCourtId)) {
            throw new GenericException(CartError.DIFFERENT_FOOD_COURT_ITEMS);
        }
        
        cart.addItem(item);
        saveCart(userId, cart);
        return cart;
        
    }
    
    public Cart updateItemQuantity(String userId, String menuItemId, int quantity) {
        Cart cart = getCart(userId);
        if (!cart.hasItem(menuItemId)) {
            throw new GenericException(CartError.ITEM_NOT_FOUND_IN_CART);
        }
        
        cart.updateItemQuantity(menuItemId, quantity);
        saveCart(userId, cart);
        if (cart.isEmpty()) {
            clearCart(userId);
        }
        return cart;
    }
    
    public void clearCart(String userId) {
        try {
            redisTemplate.delete(getCartKey(userId));
        } catch (Exception e) {
            throw new GenericException(CartError.CART_DELETE_FAILED);
        }
    }
}