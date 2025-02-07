package com.imthath.food_street.user_service.security;

import com.imthath.food_street.user_service.User;
import com.imthath.food_street.user_service.UserRepository;
import com.imthath.food_street.user_service.error.InvalidOtpException;
import com.imthath.food_street.user_service.error.InvalidTokenException;
import com.imthath.food_street.user_service.message_central.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtManager jwtManager;

    OtpResponse sendOtp(String phone) {
        String referenceId = otpService.sendOtp(phone);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("referenceId", referenceId);
        userInfo.put("phone", phone);
        String identifier = jwtManager.createToken(userInfo, 2, TimeUnit.MINUTES);
        User user = userRepository.findByPhoneNumber(phone);
        String maskedUserName = null;
        if (user != null) {
            maskedUserName = maskUserName(user.getName());
        }
        return new OtpResponse(identifier, maskedUserName);
    }

    JwtResponse validateOtp(String otp, String identifier) {
        TokenInfo parsedInfo = parseToken(identifier);
        if (!otpService.validateOtp(parsedInfo.referenceId(), otp)) {
            throw new InvalidOtpException(700);
        }
        User user = userRepository.findByPhoneNumber(parsedInfo.phone());
        if (user == null) {
            user = new User();
            user.setPhoneNumber(parsedInfo.phone());
            user = userRepository.save(user);
        }
        String token = jwtManager.createToken(user.getId(), 30, TimeUnit.DAYS);
        return new JwtResponse(token);
    }

    private String maskUserName(String name) {
        if (name.length() < 7) {
            return name.substring(0, 2) + "***";
        }
        return name.substring(0, 2) + "***" + name.substring(name.length() - 2);
    }

    private TokenInfo parseToken(String token) {
        try {
            var parsedInfo = jwtManager.parseToken(token);
            return new TokenInfo(parsedInfo.get("referenceId").toString(), parsedInfo.get("phone").toString());
        } catch (Exception e) {
            log.error("Invalid token", e);
            throw new InvalidTokenException();
        }
    }

    record TokenInfo(String referenceId, String phone) {
    }
}