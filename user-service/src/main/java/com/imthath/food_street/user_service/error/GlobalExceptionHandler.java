package com.imthath.food_street.user_service.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(GenericException exception) {
        return ResponseEntity
                .status(exception.getCode())
                .body(new ErrorResponse(exception.getMessage()));
    }

    // handles all exceptions like bad request. not ideal.
//    @ExceptionHandler(Exception.class)
//    ResponseEntity<ErrorResponse> handleException(Exception exception) {
//        return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
//    }

    public ResponseEntity<ErrorResponse> makeResponseEntity(int status, Exception exception) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(exception.getMessage()));
    }
}
