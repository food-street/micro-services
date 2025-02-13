package com.imthath.food_street.user_service.error;

import com.imthath.utils.guardrail.CommonError;
import lombok.Getter;

@Getter
public enum UserServiceError implements CommonError {
    INVALID_OTP(1000),
    INVALID_TOKEN(401),
    INTERNAL_SETUP_ERROR(1002),
    USER_NOT_FOUND(1003),
    USER_EXISTS(1004);

    private final int code;

    UserServiceError(int code) {
        this.code = code;
    }
}

