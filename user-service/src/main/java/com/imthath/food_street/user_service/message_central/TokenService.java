package com.imthath.food_street.user_service.message_central;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class TokenService {
    @Autowired
    TokenClient authClient;

    String authKey = "authToken";

    String generateAuthToken() {
        return authClient
                .generateAuthToken(Constants.CUSTOMER_ID, Constants.KEY, Constants.SCOPE)
                .data()
                .token();
    }
}
