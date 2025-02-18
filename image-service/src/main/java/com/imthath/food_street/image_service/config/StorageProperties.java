package com.imthath.food_street.image_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
    String bucketName,
    String host,
    long signedUrlExpirationMinutes
) {} 