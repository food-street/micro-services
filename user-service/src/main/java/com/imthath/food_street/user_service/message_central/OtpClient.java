package com.imthath.food_street.user_service.message_central;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

@Component
interface OtpClient {

    @PostExchange("/verification/v3/send")
    Data<OtpResponse> sendOtp(
            @RequestParam String mobileNumber,
            @RequestParam String flowType,
            @RequestParam int otpLength,
            @RequestParam String countryCode
    );

    @GetExchange("/verification/v3/validate")
    Data<VerificationResponse> validateOtp(
            @RequestParam String verificationId,
            @RequestParam String code
    );

    record Data<T>(T data) {}

    record VerificationResponse(String verificationStatus) {
        public boolean isSuccess() {
            return "VERIFICATION_COMPLETED".equals(verificationStatus);
        }
    }

    record OtpResponse(String verificationId) {}
}