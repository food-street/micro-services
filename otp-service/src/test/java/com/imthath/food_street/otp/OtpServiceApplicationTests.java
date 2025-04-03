package com.imthath.food_street.otp;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OtpServiceApplicationTests {

	@SuppressWarnings("resource")
	@ServiceConnection
	static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.2.4")).withExposedPorts(6379);

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void badRequests() {
		RestAssured
				.given()
				.post("/otp/generate")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value());

		RestAssured
				.given()
				.param("identifier", "test-bad-request")
				.put("/otp/validate")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void missingOTP() {
		RestAssured
				.given()
				.param("identifier", "test-missing")
				.param("otp", "1234")
				.put("/otp/validate")
				.then()
				.statusCode(HttpStatus.PRECONDITION_REQUIRED.value());
	}

	@Test
	void invalidOTP() {
		RestAssured
				.given()
				.param("identifier", "test-invalid")
				.post("/otp/generate")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.param("identifier", "test-invalid")
				.param("otp", "123456")
				.put("/otp/validate")
				.then()
				.statusCode(HttpStatus.CONFLICT.value());
	}

	@Test
	void validOTP() {
		var otp = RestAssured
				.given()
				.param("identifier", "test-valid")
				.post("/otp/generate")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract()
				.body()
				.jsonPath()
				.get("otp")
				.toString();

		RestAssured
				.given()
				.param("identifier", "test-valid")
				.param("otp", otp)
				.put("/otp/validate")
				.then()
				.statusCode(HttpStatus.ACCEPTED.value());

		RestAssured
				.given()
				.param("identifier", "test-valid")
				.param("otp", otp)
				.put("/otp/validate")
				.then()
				.statusCode(HttpStatus.PRECONDITION_REQUIRED.value());
	}
}
