package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.OtpGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final long OTP_EXPIRATION_MINUTES = 5;
    private static final long RATE_LIMIT_WINDOW_HOURS = 1;
    private static final int MAX_OTP_ATTEMPTS = 3;

    private static final String OTP_PREFIX = "otp:";
    private static final String RATE_LIMIT_PREFIX = "otp:ratelimit:";

    public OtpService(@Qualifier("tokenRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void generateOtp(String userIdentifier) {

        checkRateLimit(userIdentifier);

        String otpKey = OTP_PREFIX + userIdentifier;

        String existingOtp = redisTemplate.opsForValue().get(otpKey);
        if (existingOtp != null) {
            redisTemplate.delete(otpKey);
        }

        String otp = OtpGenerator.generateOtp();
        redisTemplate.opsForValue().set(otpKey, otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);

        incrementRateLimitCounter(userIdentifier);
        System.out.println("OTP sent to " + userIdentifier + ": " + otp);
    }

    public void validateOtp(String userIdentifier, String userOtp, String identifierType) {

        if(userOtp == null || userOtp.isBlank()) {
            throw new IllegalArgumentException("OTP for " +  identifierType + " verification is required.");
        }

        if(!userOtp.matches(FieldValidationConstants.OTP_REGEX)) {
            throw new IllegalArgumentException("Invalid " + identifierType + " verification OTP format.");
        }

        String otpKey = OTP_PREFIX + userIdentifier;
        String storedOtp = redisTemplate.opsForValue().get(otpKey);

        if (storedOtp == null) {
            throw new IllegalArgumentException("OTP for " + identifierType + " verification has been expired or does not exist. Please request a new OTP.");
        }

        if(!userOtp.equals(storedOtp)) {
            throw new IllegalArgumentException("Incorrect " +  identifierType + " verification OTP.");
        }

        redisTemplate.delete(otpKey);
    }

    private void incrementRateLimitCounter(String userIdentifier) {
        String rateLimitKey = RATE_LIMIT_PREFIX + userIdentifier;
        String countStr = redisTemplate.opsForValue().get(rateLimitKey);

        if (countStr == null) {
            redisTemplate.opsForValue().set(
                    rateLimitKey,
                    "1",
                    RATE_LIMIT_WINDOW_HOURS,
                    TimeUnit.HOURS
            );
        } else {
            redisTemplate.opsForValue().increment(rateLimitKey);
        }
    }

    private void checkRateLimit(String userIdentifier) {
        String rateLimitKey = RATE_LIMIT_PREFIX + userIdentifier;
        String countStr = redisTemplate.opsForValue().get(rateLimitKey);

        if (countStr != null) {
            int count = Integer.parseInt(countStr);
            if (count >= MAX_OTP_ATTEMPTS) {
                throw new IllegalArgumentException("Maximum " +
                        (userIdentifier.matches(FieldValidationConstants.PHONE_NUMBER_REGEX) ? "phone number" : "email") +
                        " verification OTP generation limit reached. Please try again after 1 hour.");
            }
        }
    }
}