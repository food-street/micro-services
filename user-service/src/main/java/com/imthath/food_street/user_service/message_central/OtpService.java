package com.imthath.food_street.user_service.message_central;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    static final String COUNTRY_CODE = "91"; // For India
    static final String FLOW_TYPE = "SMS";
    static final int OTP_LENGTH = 5;
    
    @Autowired
    private OtpClient otpClient;

    @Autowired
    private TokenService tokenService;

    public String sendOtp(String mobileNumber) {
        var response = otpClient.sendOtp(
                mobileNumber,
                FLOW_TYPE,
                OTP_LENGTH,
                COUNTRY_CODE,
                tokenService.generateAuthToken()
        );
        return response.data().verificationId();
    }

    public boolean validateOtp(String verificationId, String code) {
        var response = otpClient.validateOtp(verificationId, code, tokenService.generateAuthToken());
        return response.data().isSuccess();
    }
}