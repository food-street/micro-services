package com.imthath.food_street.menu_service;

import org.springframework.boot.SpringApplication;

public class TestMenuServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MenuServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
