package com.imthath.food_street.otp;

import com.imthath.food_street.otp.error.InvalidOtpException;
import com.imthath.food_street.otp.error.MissingOtpException;
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

    public void validateOtp(String identifier, String otp) {
        try {
            String savedOtp = redisRepository.find(identifier);
            if (!savedOtp.equals(otp)) {
                throw new InvalidOtpException();
            }
            redisRepository.delete(identifier);
        } catch (NullPointerException e) {
            throw new MissingOtpException();
        }
    }

    private String getOtp(String identifier) {
        if (redisRepository.exists(identifier)) {
            return redisRepository.find(identifier);
        }
        int maxValue = (int) Math.pow(10, otpLength);
        int randomValue = secureRandom.nextInt(maxValue);
        return String.format("%04d", randomValue);
    }
}
