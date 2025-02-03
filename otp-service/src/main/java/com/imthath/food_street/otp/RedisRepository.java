package com.imthath.food_street.otp;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

// Consider change OTP to int instead of String in Redis
@Component
public class RedisRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ValueOperations<String, String> valueOperations;

    @PostConstruct
    public void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void save(String key, String value) {
        valueOperations.set(key, value);
    }

    public String find(String key) throws NullPointerException {
        var value = valueOperations.get(key);
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public void set(String key, String value, long timeout) {
        valueOperations.set(key, value, timeout, TimeUnit.MINUTES);
    }
}
