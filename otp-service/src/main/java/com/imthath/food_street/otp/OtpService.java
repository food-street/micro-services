package com.imthath.food_street.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpService {
    @Autowired
    private RedisRepository redisRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${otp.expiration.timeout}")
    private long timeToLive;

    @Value("${otp.length}")
    private int otpLength;

    /// Generates an OTP for the given identifier and saves it in Redis.
    /// If an OTP already exists for the identifier, it returns the existing OTP and resets the expiration time.
    public String generateOtp(String identifier) {
        var otp = getOtp(identifier);
        redisRepository.set(identifier, otp, timeToLive);
        return otp;
    }

    public boolean validateOtp(String identifier, String otp) {
        try {
            String savedOtp = redisRepository.find(identifier);
            if (savedOtp.equals(otp)) {
                redisRepository.delete(identifier);
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            System.out.println("OTP not found for identifier " + identifier);
            return false;
        }
    }

    private String getOtp(String identifier) {
        if (redisRepository.exists(identifier)) {
            return redisRepository.find(identifier);
        }
        int maxValue = (int) Math.pow(10, otpLength) - 1;
        return String.valueOf(secureRandom.nextInt(maxValue));
    }
}
