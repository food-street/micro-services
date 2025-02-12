package com.imthath.food_street.user_service.error;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException {
    private final int code;

    public GenericException(int statusCode, CommonError error) {
        super(error.name());
        this.code = statusCode;
    }

    public GenericException(CommonError error) {
        super(error.name());
        this.code = error.getCode();
    }

    public GenericException(HttpStatus httpStatus, CommonError error) {
        super(error.name());
        this.code = httpStatus.value();
    }

    public int getCode() {
        return code;
    }
}

