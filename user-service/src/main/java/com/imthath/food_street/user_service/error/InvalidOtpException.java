package com.imthath.food_street.user_service.error;

import lombok.Getter;

public class InvalidOtpException extends RuntimeException {
    @Getter int statusCode;

    public InvalidOtpException(int statusCode) {
        super("Invalid otp");
        this.statusCode = statusCode;
    }
}
