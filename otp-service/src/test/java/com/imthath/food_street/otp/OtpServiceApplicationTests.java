package com.imthath.food_street.otp;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OtpServiceApplicationTests {

	@SuppressWarnings("resource")
	@ServiceConnection
	static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6.2.6")).withExposedPorts(6379);

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
				.statusCode(400);

		RestAssured
				.given()
				.param("identifier", "test-bad-request")
				.put("/otp/validate")
				.then()
				.statusCode(400);
	}

	@Test
	void missingOTP() {
		RestAssured
				.given()
				.param("identifier", "test-missing")
				.param("otp", "1234")
				.put("/otp/validate")
				.then()
				.statusCode(428);
	}

	@Test
	void invalidOTP() {
		RestAssured
				.given()
				.param("identifier", "test-invalid")
				.post("/otp/generate")
				.then()
				.statusCode(201);

		RestAssured
				.given()
				.param("identifier", "test-invalid")
				.param("otp", "123456")
				.put("/otp/validate")
				.then()
				.statusCode(409);
	}

	@Test
	void validOTP() {
		var otp = RestAssured
				.given()
				.param("identifier", "test-valid")
				.post("/otp/generate")
				.then()
				.statusCode(201)
				.extract()
				.body()
				.asString();

		RestAssured
				.given()
				.param("identifier", "test-valid")
				.param("otp", otp)
				.put("/otp/validate")
				.then()
				.statusCode(202);

		RestAssured
				.given()
				.param("identifier", "test-valid")
				.param("otp", otp)
				.put("/otp/validate")
				.then()
				.statusCode(428);
	}
}
