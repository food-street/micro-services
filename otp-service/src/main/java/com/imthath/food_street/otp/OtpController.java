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
    OtpResponse generateOtp(@RequestParam String identifier) {
        return new OtpResponse(otpService.generateOtp(identifier));
    }

    @PutMapping("/validate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void validateOtp(@RequestParam String identifier, @RequestParam String otp) {
        otpService.validateOtp(identifier, otp);
    }

    record OtpResponse(String otp) {}
}
