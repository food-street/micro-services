package com.imthath.food_street.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OtpController {
    @Autowired private OtpService otpService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public String generateOtp(@RequestParam String identifier) {
        return otpService.generateOtp(identifier);
    }

    @PutMapping("/validate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean validateOtp(@RequestParam String identifier, @RequestParam String otp) {
        return otpService.validateOtp(identifier, otp);
    }
}
