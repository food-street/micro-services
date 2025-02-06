package com.imthath.food_street.user_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceApplicationTests {

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void invalidPhoneNumbers() {
		RestAssured.given()
				.param("phone", "123")
				.post("/auth/v1/send-otp")
				.then()
				.statusCode(400);

		RestAssured.given()
				.param("phone", "testing")
				.post("/auth/v1/send-otp")
				.then()
				.statusCode(400);
	}
}
