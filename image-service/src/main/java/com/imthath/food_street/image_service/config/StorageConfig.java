package com.imthath.food_street.image_service.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfig {
    
    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        if (credentials != null) {
            System.out.println("Objectained credentials for project "  + credentials.getQuotaProjectId());
        }
        return StorageOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .getService();
    }
} 