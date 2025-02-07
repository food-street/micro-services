package com.imthath.food_street.user_service.security;

import java.security.*;
import java.util.Base64;

public class KeyGen {
    static void generateKeys() {

        try {
            // Generate the key pair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Get both public and private keys
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Convert the public key to PEM format
            String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getMimeEncoder().encodeToString(publicKey.getEncoded()) +
                    "\n-----END PUBLIC KEY-----";

            // Convert the private key to PEM format
            String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                    Base64.getMimeEncoder().encodeToString(privateKey.getEncoded()) +
                    "\n-----END PRIVATE KEY-----";

            // Print both keys
            System.out.println("Public Key:");
            System.out.println(publicKeyPem);
            System.out.println("\nPrivate Key:");
            System.out.println(privateKeyPem);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error generating keys: " + e.getMessage());
        }
    }
}
