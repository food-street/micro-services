package com.imthath.food_street.user_service.error;

import com.imthath.utils.guardrail.CommonError;
import lombok.Getter;

@Getter
public enum UserServiceError implements CommonError {
    INVALID_OTP(900),
    INVALID_TOKEN(401),
    INTERNAL_SETUP_ERROR(902),
    USER_NOT_FOUND(903),
    USER_EXISTS(904);

    private final int code;

    UserServiceError(int code) {
        this.code = code;
    }
}

