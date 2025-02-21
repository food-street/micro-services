package com.imthath.food_street.menu_service;

import com.imthath.utils.guardrail.CommonError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuError implements CommonError {
    RESTAURANT_NOT_FOUND(901),
    RESTAURANT_MISMATCH(902),
    CATEGORY_NOT_FOUND(903),
    ITEM_NOT_FOUND(904);

    private final int code;
}
