package com.imthath.food_street.user_service.message_central;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class TokenService {
    String CUSTOMER_ID;
    String KEY;
    String SCOPE = "NEW";

    @PostConstruct
    void init() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        CUSTOMER_ID = dotenv.get("MC_CUSTOMER_ID");
        KEY = dotenv.get("MC_KEY");
    }

    @Autowired
    TokenClient tokenClient;

    static final String HEADER_KEY = "authToken";

    String generateAuthToken() {
        try {
            return tokenClient.generateAuthToken(CUSTOMER_ID, SCOPE, KEY).token();
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
    }
}
