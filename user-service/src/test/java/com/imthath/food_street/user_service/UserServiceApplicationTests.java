package com.imthath.food_street.user_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
				.post("/auth/send-otp")
				.then()
				.statusCode(400);

		RestAssured.given()
				.param("phone", "testing")
				.post("/auth/send-otp")
				.then()
				.statusCode(400);
	}

	@Test
	void validPhoneNumber() {
		String PHONE = "1234567890";
		var verificationId = MessageCentralClient.stubSendOtp(PHONE);
		MessageCentralClient.stubValidOtp(verificationId);

		var bodyJson = RestAssured
				.given()
				.param("phone", PHONE)
				.post("/auth/send-otp")
				.then()
				.statusCode(201)
				.extract()
				.body()
				.jsonPath();
		var identifier = bodyJson.get("identifier").toString();
		var maskedUserName = bodyJson.get("maskedUserName");

		assert maskedUserName == null;

		var token = RestAssured.given()
				.param("otp", MessageCentralClient.VALID_OTP)
				.header("identifier", identifier)
				.put("/auth/validate-otp")
				.then()
				.statusCode(202)
				.extract()
				.body()
				.jsonPath()
				.get("token");

		assert token != null;
	}

	@Test
	void invalidJWT() {
		RestAssured.given()
				.param("otp", "12345")
				.header("identifier", "12345")
				.put("/auth/validate-otp")
				.then()
				.statusCode(401);
	}

	@Test
	void invalidOtp() {
		String PHONE = "1234567890";
		var verificationId = MessageCentralClient.stubSendOtp(PHONE);

		var bodyJson = RestAssured
				.given()
				.param("phone", PHONE)
				.post("/auth/send-otp")
				.then()
				.statusCode(201)
				.extract()
				.body()
				.jsonPath();
		var identifier = bodyJson.get("identifier").toString();

		testInvalidOtp(MessageCentralClient.WRONG_OTP, verificationId, identifier);
		testInvalidOtp(MessageCentralClient.ALREADY_VERIFIED_OTP, verificationId, identifier);
		testInvalidOtp(MessageCentralClient.EXPIRED_OTP, verificationId, identifier);

	}

	private void testInvalidOtp(String otp, String verificationId, String identifier) {
		MessageCentralClient.stubInvalidOtp(verificationId, otp);

		RestAssured.given()
				.param("otp", otp)
				.header("identifier", identifier)
				.put("/auth/validate-otp")
				.then()
				.statusCode(MessageCentralClient.getInvalidStatus(otp));
	}
}
