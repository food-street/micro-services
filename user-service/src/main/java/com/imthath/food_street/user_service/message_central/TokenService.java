package com.imthath.food_street.user_service.message_central;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class TokenService {

//    @Value("${MC_CUSTOMER_ID}")
//    private String CUSTOMER_ID;
//
//    @Value("${MC_KEY}")
//    private String KEY;

    @Autowired
    TokenClient tokenClient;

    static final String HEADER_KEY = "authToken";

    @Value("${MC_TOKEN}")
    static String AUTH_TOKEN;

//    String generateAuthToken() {
//        return tokenClient.generateAuthToken(CUSTOMER_ID, "NEW", KEY).token();
//    }
}
