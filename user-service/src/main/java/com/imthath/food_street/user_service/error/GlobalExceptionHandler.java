package com.imthath.food_street.user_service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    ResponseEntity<GenericError> handleInvalidToken(InvalidTokenException exception) {
        return makeResponseEntity(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(InvalidOtpException.class)
    ResponseEntity<GenericError> handleInvalidOtp(InvalidOtpException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(new GenericError(exception.getMessage()));
    }

    private ResponseEntity<GenericError> makeResponseEntity(HttpStatus status, Exception exception) {
        return ResponseEntity
                .status(status)
                .body(new GenericError(exception.getMessage()));
    }
}
