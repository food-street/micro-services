package com.imthath.food_street.api_gateway;

import com.imthath.utils.guardrail.ErrorResponse;
import com.imthath.utils.guardrail.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.channels.ClosedChannelException;

@ControllerAdvice
public class GatewayExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(ClosedChannelException.class)
    public ResponseEntity<ErrorResponse> handleClosedChannelException(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(e.getMessage()));
    }
}
