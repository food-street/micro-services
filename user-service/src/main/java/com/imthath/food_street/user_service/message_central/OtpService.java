package com.imthath.food_street.user_service.message_central;

import com.imthath.food_street.user_service.error.UserServiceError;
import com.imthath.utils.guardrail.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OtpService {
    static final String COUNTRY_CODE = "91"; // For India
    static final String FLOW_TYPE = "SMS";
    static final int OTP_LENGTH = 5;

    @Value("${MC_TOKEN}")
    String AUTH_TOKEN;

//    @Autowired
//    private TokenService tokenService;

    @Autowired
    private OtpClient otpClient;

    public String sendOtp(String mobileNumber) {
        var response = otpClient.sendOtp(
                mobileNumber,
                FLOW_TYPE,
                OTP_LENGTH,
                COUNTRY_CODE,
                AUTH_TOKEN
        );
        return response.data().verificationId();
    }

    public boolean validateOtp(String verificationId, String code) {
        var response = otpClient.validateOtp(verificationId, code, AUTH_TOKEN);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new GenericException(response.getStatusCode().value(), UserServiceError.INVALID_OTP);
        }
        try {
            return Objects.requireNonNull(response.getBody()).data().isSuccess();
        } catch (NullPointerException e) {
            throw new GenericException(UserServiceError.INVALID_OTP);
        }
    }
}