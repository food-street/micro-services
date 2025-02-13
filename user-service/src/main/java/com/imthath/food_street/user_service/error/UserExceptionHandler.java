package com.imthath.food_street.user_service.error;

import com.imthath.utils.guardrail.ErrorResponse;
import com.imthath.utils.guardrail.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.channels.UnresolvedAddressException;

@ControllerAdvice
public class UserExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(UnresolvedAddressException.class)
    ResponseEntity<ErrorResponse> handleUnresolvedAddressException(UnresolvedAddressException exception) {
        return makeResponseEntity(HttpStatus.SERVICE_UNAVAILABLE.value(), exception);
    }
}
