package com.imthath.food_street.otp.error;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException() {
        super("invalid otp");
    }
}
