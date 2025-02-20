package com.imthath.food_street.restaurant_service.error;

import com.imthath.utils.guardrail.CommonError;
import lombok.Getter;

@Getter
public enum RestaurantError implements CommonError {
    COURT_NOT_FOUND(901),
    RESTAURANT_NOT_FOUND(902),
    COURT_ID_MISMATCH(903);

    private final int code;

    RestaurantError(int code) {
        this.code = code;
    }
} 