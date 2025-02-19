package com.imthath.food_street.restaurant_service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    public MySQLContainer<?> mySQLContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"));
//                .withDatabaseName("restaurant_service_test")
//                .withUsername("test")
//                .withPassword("test");
    }
} 