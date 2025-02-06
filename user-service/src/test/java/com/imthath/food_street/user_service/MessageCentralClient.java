package com.imthath.food_street.user_service;

import java.security.SecureRandom;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class MessageCentralClient {
    private static final SecureRandom secureRandom = new SecureRandom();

    static String stubSendOtp(String phone) {
        var urlPattern = urlEqualTo("/verification/v3/send?mobileNumber="+ phone + "&flowType=SMS&otpLength=5&countryCode=91");
        var verificationId = generateRandomId();
        var response = aResponse()
                .withBody("{\"data\":{\"verificationId\":\"" + verificationId + "\"}}")
                .withHeader("Content-Type", "application/json")
                .withStatus(200);
        stubFor(post(urlPattern).willReturn(response));
        return verificationId;
    }

    static void stubValidateOtp(String verificationId, String otp) {
        var urlPattern = urlEqualTo("/verification/v3/validateOtp?verificationId=" + verificationId + "&code=" + otp);
        var response = aResponse()
                .withBody("{\"data\":{\"verificationStatus\":\"VERIFICATION_COMPLETED\"}}")
                .withHeader("Content-Type", "application/json")
                .withStatus(200);
        stubFor(get(urlPattern).willReturn(response));
    }

    private static String generateRandomId() {
        int maxValue = (int) Math.pow(10, 8);
        int randomValue = secureRandom.nextInt(maxValue);
        return String.format("%08d", randomValue);
    }
}
