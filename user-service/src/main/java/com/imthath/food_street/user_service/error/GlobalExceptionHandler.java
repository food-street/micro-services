package com.imthath.food_street.user_service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.channels.UnresolvedAddressException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GenericException.class)
    ResponseEntity<ErrorResponse> handleGenericException(GenericException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(UnresolvedAddressException.class)
    ResponseEntity<ErrorResponse> handleUnresolvedAddressException(UnresolvedAddressException exception) {
        return makeResponseEntity(HttpStatus.SERVICE_UNAVAILABLE, exception);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private ResponseEntity<ErrorResponse> makeResponseEntity(HttpStatus status, Exception exception) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(exception.getMessage()));
    }
}
