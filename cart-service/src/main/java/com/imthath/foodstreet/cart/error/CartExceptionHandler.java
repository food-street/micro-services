package com.imthath.foodstreet.cart.error;

import com.imthath.utils.guardrail.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.imthath.utils.guardrail.GlobalExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class CartExceptionHandler extends GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        return super.makeResponseEntity(HttpStatus.BAD_REQUEST.value(), exception);
    }
}
