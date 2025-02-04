package com.imthath.food_street.user_service.message_central;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    @Autowired
    private OtpClient otpClient;

    public String sendOtp(String mobileNumber) {
        var response = otpClient.sendOtp(
                mobileNumber,
                Constants.FLOW_TYPE,
                Constants.OTP_LENGTH,
                Constants.COUNTRY_CODE
        );
        return response.data().verificationId();
    }

    public boolean validateOtp(String verificationId, String code) {
        var response = otpClient.validateOtp(verificationId, code);
        return response.data().isSuccess();
    }
}