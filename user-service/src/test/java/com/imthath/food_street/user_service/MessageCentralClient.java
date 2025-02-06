package com.imthath.food_street.user_service;

import java.security.SecureRandom;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class MessageCentralClient {
    private static final SecureRandom secureRandom = new SecureRandom();

    static String stubSendOtp(String phone) {
        var urlPattern = urlEqualTo("/send?countryCode=91&flowType=SMS&mobileNumber="+ phone + "&otpLength=5");
        var verificationId = generateRandomId();
        var response = aResponse()
                .withBody("{\"data\":{\"verificationId\":\"" + verificationId + "\"}}")
                .withHeader("Content-Type", "application/json")
                .withStatus(200);
        stubFor(post(urlPattern).willReturn(response));
        return verificationId;
    }

    static void stubValidateOtp(String verificationId, String otp) {
        var urlPattern = urlEqualTo("/validateOtp?code=" + otp + "&verificationId=" + verificationId);
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
