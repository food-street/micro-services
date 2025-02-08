package com.imthath.food_street.user_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/send-otp")
    @ResponseStatus(HttpStatus.CREATED)
    OtpResponse sendOtp(@RequestParam @ValidPhoneNumber String phone) {
        return authService.sendOtp(phone);
    }

    @PutMapping("/validate-otp")
    @ResponseStatus(HttpStatus.ACCEPTED)
    JwtResponse validateOtp(@RequestParam String otp, @RequestHeader("identifier") String identifier) {
        return authService.validateOtp(otp, identifier);
    }
}
