package com.imthath.food_street.user_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/send-otp")
    OtpResponse sendOtp(@RequestParam @ValidPhoneNumber String phone) {
        return authService.sendOtp(phone);
    }

    @PutMapping("/validate-otp")
    JwtResponse validateOtp(@RequestParam String otp, @RequestHeader("identifier") String identifier) {
        return authService.validateOtp(otp, identifier);
    }
}
