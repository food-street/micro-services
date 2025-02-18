package com.imthath.food_street.image_service.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfig {
    
    @Value("${google.cloud.credentials.location:classpath:service-account-key.json}")
    private String credentialsLocation;
    
    private final ResourceLoader resourceLoader;
    
    public StorageConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    @Bean
    public Storage storage() throws IOException {
        Resource credentialsResource = resourceLoader.getResource(credentialsLocation);
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(credentialsResource.getInputStream());
        
        return StorageOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .getService();
    }
}