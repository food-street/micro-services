package com.imthath.food_street.user_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
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

	@Test
	void validPhoneNumber() {
		String PHONE = "1234567890";
		String OTP = "12346";
		var verificationId = MessageCentralClient.stubSendOtp(PHONE);
		MessageCentralClient.stubValidateOtp(verificationId, OTP);

		var bodyJson = RestAssured
				.given()
				.param("phone", PHONE)
				.header("authToken", "random-token")
				.post("/auth/v1/send-otp")
				.then()
				.statusCode(201)
				.extract()
				.body()
				.jsonPath();
		var identifier = bodyJson.get("identifier");
		var maskedUserName = bodyJson.get("maskedUserName");

		assert maskedUserName == null;

		var token = RestAssured.given()
				.param("otp", OTP)
				.header("identifier", identifier)
				.put("/auth/v1/validate-otp")
				.then()
				.statusCode(202)
				.extract()
				.body()
				.jsonPath()
				.get("token");

		assert token != null;
	}
}
