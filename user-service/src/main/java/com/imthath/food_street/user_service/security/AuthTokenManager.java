package com.imthath.food_street.user_service.security;

import com.imthath.food_street.user_service.helper.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class AuthTokenManager {

    @Value("${AUTH_TOKEN_SECRET}")
    private String secretKey;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createShortLivedToken(Map<String, String> claims) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.now)
                .expiration(Date.now.adding(2, TimeUnit.MINUTES))
                .signWith(key)
                .compact();
    }

    Map<String, Object> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}