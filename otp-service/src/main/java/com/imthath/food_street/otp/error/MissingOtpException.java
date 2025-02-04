package com.imthath.food_street.otp.error;

public class MissingOtpException extends RuntimeException {
    public MissingOtpException() {
        super("missing otp");
    }
}
