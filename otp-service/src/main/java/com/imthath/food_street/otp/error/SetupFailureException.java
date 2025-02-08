package com.imthath.food_street.otp.error;

public class SetupFailureException extends RuntimeException {
    public SetupFailureException(String message) {
        super("Setup failure: " + message);
    }
}
