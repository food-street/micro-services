package com.imthath.food_street.otp.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(InvalidOtpException.class)
//    public ResponseEntity<String> handleNullPointerException(InvalidOtpException exception) {
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
//    }

//    @ExceptionHandler(InvalidOtpException.class)
//    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
//    public void handleNullPointerException() {
//    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<GenericError> handleNullPointerException(InvalidOtpException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(new GenericError(exception.getMessage()));
    }
}
