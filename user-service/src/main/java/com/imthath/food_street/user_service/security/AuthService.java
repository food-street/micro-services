package com.imthath.food_street.user_service.security;

import com.imthath.food_street.user_service.User;
import com.imthath.food_street.user_service.UserRepository;
import com.imthath.food_street.user_service.error.UserServiceError;
import com.imthath.food_street.user_service.message_central.OtpService;
import com.imthath.utils.guardrail.GenericException;
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

     UserTokenProvider getUserTokenProvider() {
         return userTokenProvider;
     }

    OtpResponse sendOtp(String phone) {
        String referenceId = otpService.sendOtp(phone);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("referenceId", referenceId);
        userInfo.put("phone", phone);
        String identifier = authTokenManager.createShortLivedToken(userInfo);
        Optional<User> user = userRepository.findByPhoneNumber(phone);
        String maskedUserName = null;
        if (user.isPresent()) {
            maskedUserName = maskUserName(user.get().getName());
        }
        return new OtpResponse(identifier, maskedUserName);
    }

    JwtResponse validateOtp(String otp, String identifier) {
        AuthTokenInfo parsedInfo = parseToken(identifier);
        if (!otpService.validateOtp(parsedInfo.referenceId(), otp)) {
            throw new GenericException(700, UserServiceError.INVALID_OTP);
        }
        var user = userRepository.findByPhoneNumber(parsedInfo.phone());
        if (user.isEmpty()) {
            user = Optional.of(new User());
            user.get().setPhoneNumber(parsedInfo.phone());
            user.get().setRole(User.Role.USER);
            userRepository.save(user.get());
        }
        try {
            User.Role role = user.get().getRole();
            String token = userTokenProvider.createToken(user.get().getId(), role.toString());
            return new JwtResponse(token);
        } catch (Exception e) {
            log.error("Failed to create token", e);
            throw new GenericException(600, UserServiceError.INTERNAL_SETUP_ERROR);
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
            throw new GenericException(UserServiceError.INVALID_TOKEN);
        }
    }

    record AuthTokenInfo(String referenceId, String phone) {
    }
}