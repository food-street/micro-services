package com.imthath.food_street.user_service.security;

import com.imthath.food_street.user_service.User;
import com.imthath.food_street.user_service.UserRepository;
import com.imthath.food_street.user_service.message_central.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public OtpResponse sendOtp(String phone) {
        String referenceId = otpService.sendOtp(phone);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("referenceId", referenceId);
        userInfo.put("phone", phone);
        String identifier = jwtTokenProvider.createToken(userInfo, 2, TimeUnit.MINUTES);
        User user = userRepository.findByPhoneNumber(phone);
        String maskedUserName = null;
        if (user != null) {
            maskedUserName = maskUserName(user.getName());
        }
        return new OtpResponse(identifier, maskedUserName);
    }

    public JwtResponse validateOtp(String identifier, String otp) {
        var parsedInfo = jwtTokenProvider.parseToken(identifier);
        String referenceId = parsedInfo.get("referenceId").toString();
        String phone = parsedInfo.get("phone").toString();
        boolean isValid = otpService.validateOtp(referenceId, otp);
        if (isValid) {
            User user = userRepository.findByPhoneNumber(phone);
            String token = jwtTokenProvider.createToken(user.getId(), 30, TimeUnit.DAYS);
            return new JwtResponse(token);
        }
        throw new IllegalArgumentException("Invalid OTP");
    }

    private String maskUserName(String name) {
        if (name.length() < 7) {
            return name.substring(0, 2) + "***";
        }
        return name.substring(0, 2) + "***" + name.substring(name.length() - 2);
    }

    public record OtpResponse(String identifier, String maskedUserName) {}
    public record JwtResponse(String token) {}
}