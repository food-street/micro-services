package com.imthath.food_street.user_service.security;

import com.imthath.food_street.user_service.User;
import com.imthath.food_street.user_service.UserRepository;
import com.imthath.food_street.user_service.error.CommonError;
import com.imthath.food_street.user_service.error.GenericException;
import com.imthath.food_street.user_service.error.InvalidTokenException;
import com.imthath.food_street.user_service.message_central.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthTokenManager authTokenManager;

    @Autowired
    private UserTokenProvider userTokenProvider;

    // UserTokenProvider getUserTokenProvider() {
    //     return userTokenProvider;
    // }

    OtpResponse sendOtp(String phone) {
        String referenceId = otpService.sendOtp(phone);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("referenceId", referenceId);
        userInfo.put("phone", phone);
        String identifier = authTokenManager.createShortLivedToken(userInfo);
        User user = userRepository.findByPhoneNumber(phone);
        String maskedUserName = null;
        if (user != null) {
            maskedUserName = maskUserName(user.getName());
        }
        return new OtpResponse(identifier, maskedUserName);
    }

    JwtResponse validateOtp(String otp, String identifier) {
        AuthTokenInfo parsedInfo = parseToken(identifier);
        if (!otpService.validateOtp(parsedInfo.referenceId(), otp)) {
            throw new GenericException(700, CommonError.INVALID_OTP);
        }
        User user = userRepository.findByPhoneNumber(parsedInfo.phone());
        if (user == null) {
            user = new User();
            user.setPhoneNumber(parsedInfo.phone());
            user = userRepository.save(user);
        }
        try {
            String role = Optional.ofNullable(user.getRole()).orElse("USER");
            String token = userTokenProvider.createToken(user.getId(), role);
            return new JwtResponse(token);
        } catch (Exception e) {
            log.error("Failed to create token", e);
            throw new GenericException(600, CommonError.INTERNAL_SETUP_ERROR);
        }
    }

    private String maskUserName(String name) {
        if (name.length() < 7) {
            return name.substring(0, 2) + "***";
        }
        return name.substring(0, 2) + "***" + name.substring(name.length() - 2);
    }

    private AuthTokenInfo parseToken(String token) {
        try {
            var parsedInfo = authTokenManager.parseToken(token);
            return new AuthTokenInfo(parsedInfo.get("referenceId").toString(), parsedInfo.get("phone").toString());
        } catch (Exception e) {
            log.error("Invalid token", e);
            throw new InvalidTokenException();
        }
    }

    record AuthTokenInfo(String referenceId, String phone) {
    }
}