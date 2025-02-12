package com.imthath.food_street.user_service.error;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends GenericException {
    public InvalidTokenException() {
        super(HttpStatus.UNAUTHORIZED, UserServiceError.INVALID_TOKEN);
    }
}
