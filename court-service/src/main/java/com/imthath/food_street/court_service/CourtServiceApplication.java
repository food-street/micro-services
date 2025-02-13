package com.imthath.food_street.court_service;

import com.imthath.utils.guardrail.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CourtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourtServiceApplication.class, args);
	}

	@Bean
	public GlobalExceptionHandler globalExceptionHandler() { return new GlobalExceptionHandler(); }

}
