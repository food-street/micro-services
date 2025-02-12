package com.imthath.food_street.user_service.error;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException {
    private final int code;

    public GenericException(int statusCode, UserServiceError error) {
        super(error.name());
        this.code = statusCode;
    }

    public GenericException(UserServiceError error) {
        super(error.name());
        this.code = error.getCode();
    }

    public GenericException(HttpStatus httpStatus, UserServiceError error) {
        super(error.name());
        this.code = httpStatus.value();
    }

    public int getCode() {
        return code;
    }
}

