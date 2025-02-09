package com.imthath.food_street.api_gateway.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class TokenParser {
    @Value("${JWT_PUBLIC_KEY}")
    private String keyPem;

    public TokenInfo parseToken(String token) throws Exception {
        var claims = Jwts.parser()
                .verifyWith(decodePublicKey(keyPem))
                .build()
                .parseSignedClaims(token).getPayload();
        return new TokenInfo(claims.getSubject(), claims.getAudience());
    }

    private PublicKey decodePublicKey(String publicKeyPem) throws Exception {
        // Remove the PEM headers and newlines
        String publicKeyContent = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        // Decode the Base64 content
        byte[] decoded = Base64.getMimeDecoder().decode(publicKeyContent);

        // Create the public key specification
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

        // Get the key factory and generate the public key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

}
