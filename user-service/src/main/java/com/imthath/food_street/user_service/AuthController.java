package com.imthath.food_street.user_service;

import com.imthath.food_street.user_service.message_central.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    @Autowired
    OtpService otpService;

    @PostMapping("/send-otp")
    public OtpResponse sendOtp(@RequestParam String phone) {
        return new OtpResponse(otpService.sendOtp(phone));
    }

    @PutMapping("/validate-otp")
    public boolean validateOtp(@RequestParam String referenceId, @RequestParam String otp) {
        return otpService.validateOtp(referenceId, otp);
    }

    record OtpResponse(String referenceId) {}
}
