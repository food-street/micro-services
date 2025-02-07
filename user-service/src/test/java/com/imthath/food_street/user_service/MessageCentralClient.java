package com.imthath.food_street.user_service;

import java.security.SecureRandom;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class MessageCentralClient {
    private static final SecureRandom secureRandom = new SecureRandom();
    static final String VALID_OTP = "12345";
    static final String ALREADY_VERIFIED_OTP = "23456";
    static final String EXPIRED_OTP = "34556";
    static final String WRONG_OTP = "78901";

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

    static void stubValidOtp(String verificationId) {
        var urlPattern = urlEqualTo("/verification/v3/validateOtp?verificationId=" + verificationId + "&code=" + VALID_OTP);
        var response = aResponse()
                .withBody("{\"data\":{\"verificationStatus\":\"VERIFICATION_COMPLETED\"}}")
                .withHeader("Content-Type", "application/json")
                .withStatus(200);
        stubFor(get(urlPattern).willReturn(response));
    }

    static void stubInvalidOtp(String verificationId, String otp) {
        var urlPattern = urlEqualTo("/verification/v3/validateOtp?verificationId=" + verificationId + "&code=" + otp);
        var response = aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(getInvalidStatus(otp));
        stubFor(get(urlPattern).willReturn(response));
    }

    static int getInvalidStatus(String otp) {
        return switch (otp) {
            case ALREADY_VERIFIED_OTP -> 703;
            case EXPIRED_OTP -> 705;
            case WRONG_OTP -> 702;
            default -> 200;
        };
    }

    private static String generateRandomId() {
        int maxValue = (int) Math.pow(10, 8);
        int randomValue = secureRandom.nextInt(maxValue);
        return String.format("%08d", randomValue);
    }
}
