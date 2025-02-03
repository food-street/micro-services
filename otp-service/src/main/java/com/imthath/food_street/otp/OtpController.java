package com.imthath.food_street.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
public class OtpController {
    @Autowired private OtpService otpService;

    @RequestMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public String generateOtp(@RequestParam String identifier) {
        return otpService.generateOtp(identifier);
    }

    @RequestMapping("/validate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean validateOtp(@RequestParam String identifier, @RequestParam String otp) {
        return otpService.validateOtp(identifier, otp);
    }
}
