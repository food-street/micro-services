package com.imthath.food_street.user_service.message_central;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

@Component
interface TokenClient {
    @GetExchange("/auth/v1/authentication/token")
    TokenResponse generateAuthToken(
            @RequestParam String customerId,
            @RequestParam String scope,
            @RequestParam String key
    );

    record TokenResponse(int status, String token) {}
}


