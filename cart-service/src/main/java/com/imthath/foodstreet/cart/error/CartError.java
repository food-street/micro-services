package com.imthath.foodstreet.cart.error;

import com.imthath.utils.guardrail.CommonError;
import lombok.Getter;

@Getter
public enum CartError implements CommonError {
    COURT_NOT_FOUND(901),                 // Reusing court service error code
    CART_NOT_FOUND(902),                  // Following similar pattern as RESTAURANT_NOT_FOUND
    DIFFERENT_FOOD_COURT_ITEMS(903),      // Similar to COURT_ID_MISMATCH
    ITEM_NOT_FOUND_IN_CART(904),         // Following similar pattern as ITEM_NOT_FOUND in menu service
    CART_READ_FAILED(905),
    CART_SAVE_FAILED(906),              // Reusing cart service error code
    CART_DELETE_FAILED(906);

    private final int code;

    CartError(int code) {
        this.code = code;
    }
}