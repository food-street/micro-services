package com.imthath.food_street.user_service.error;

import lombok.Getter;

@Getter
public enum CommonError {
    INVALID_OTP(1000),
    INVALID_TOKEN(1001),
    INTERNAL_SETUP_ERROR(1002),
    USER_NOT_FOUND(1003),
    USER_EXISTS(1004);

    private final int code;

    CommonError(int code) {
        this.code = code;
    }
}

