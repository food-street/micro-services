package com.imthath.food_street.court_service;

import org.springframework.boot.SpringApplication;

public class TestCourtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(CourtServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
