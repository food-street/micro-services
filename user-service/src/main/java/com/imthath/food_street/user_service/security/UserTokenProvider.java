package com.imthath.food_street.user_service.security;

import com.imthath.food_street.user_service.helper.Date;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
public class UserTokenProvider {
    @Value("${JWT_PRIVATE_KEY}")
    private String keyPem;

    String createToken(String subject, String audience) throws Exception {
        return Jwts.builder()
                .subject(subject)
                .audience().add(audience).and()
                .issuedAt(Date.now)
                .expiration(Date.now.adding(30, TimeUnit.DAYS))
                .signWith(decodePrivateKey(keyPem))
                .compact();
    }

    private PrivateKey decodePrivateKey(String privateKeyPem) throws Exception {
        // Remove the PEM headers and newlines
        String privateKeyContent = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        // Decode the Base64 content
        byte[] decoded = Base64.getDecoder().decode(privateKeyContent);

        // Create the private key specification
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        // Get the key factory and generate the private key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}

