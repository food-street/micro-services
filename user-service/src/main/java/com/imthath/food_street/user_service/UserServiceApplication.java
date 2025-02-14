package com.imthath.food_street.user_service;

import com.imthath.utils.guardrail.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	GlobalExceptionHandler exceptionHandler() {
		return new GlobalExceptionHandler();
	}
}
