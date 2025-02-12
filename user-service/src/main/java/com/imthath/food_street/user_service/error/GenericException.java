package com.imthath.food_street.user_service.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException {
    @Getter int statusCode;

    public GenericException(int statusCode, UserServiceError error) {
        super(error.name());
        this.statusCode = statusCode;
    }

    public GenericException(UserServiceError error) {
        super(error.name());
        this.statusCode = error.getCode();
    }

    public GenericException(HttpStatus httpStatus, UserServiceError error) {
        super(error.name());
        this.statusCode = httpStatus.value();
    }
}

