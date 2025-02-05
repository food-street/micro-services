package com.imthath.food_street.user_service.message_central;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class TokenService {
    static final String CUSTOMER_ID = System.getenv("MC_CUSTOMER_ID");
    static final String KEY = System.getenv("MC_KEY");
    static final String SCOPE = "NEW";

    @Autowired
    TokenClient tokenClient;

    String key = "authToken";

    String generateAuthToken() {
        try {
            return tokenClient.generateAuthToken(CUSTOMER_ID, SCOPE, KEY).token();
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
    }
}
