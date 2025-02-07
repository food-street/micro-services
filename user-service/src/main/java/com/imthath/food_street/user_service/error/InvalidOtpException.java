package com.imthath.food_street.user_service.error;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException() {
        super("Invalid otp");
    }
}
