package com.imthath.food_street.otp.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

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
    public ResponseEntity<ErrorResponse> handleInvalidOtp(InvalidOtpException exception) {
        return makeResponseEntity(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(MissingOtpException.class)
    public ResponseEntity<ErrorResponse> handleMissingOtp(MissingOtpException exception) {
        return makeResponseEntity(HttpStatus.PRECONDITION_REQUIRED, exception);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponse> handleConnectException(ConnectException exception) {
        return makeResponseEntity(HttpStatus.NOT_IMPLEMENTED, exception);
    }

    private ResponseEntity<ErrorResponse> makeResponseEntity(HttpStatus status, Exception exception) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(exception.getMessage()));
    }
}
