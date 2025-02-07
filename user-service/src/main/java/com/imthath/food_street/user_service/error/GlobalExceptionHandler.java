package com.imthath.food_street.user_service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GenericException.class)
    ResponseEntity<ErrorResponse> handleGenericException(GenericException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(new ErrorResponse(exception.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeResponseEntity(HttpStatus status, Exception exception) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(exception.getMessage()));
    }
}
