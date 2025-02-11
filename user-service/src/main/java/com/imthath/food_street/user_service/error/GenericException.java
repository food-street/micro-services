package com.imthath.food_street.user_service.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class GenericException extends RuntimeException {
    @Getter int statusCode;

    public GenericException(int statusCode, CommonError error) {
        super(error.name());
        this.statusCode = statusCode;
    }

    public GenericException(CommonError error) {
        super(error.name());
        this.statusCode = error.getCode();
    }

    public GenericException(HttpStatus httpStatus, CommonError error) {
        super(error.name());
        this.statusCode = httpStatus.value();
    }
}

