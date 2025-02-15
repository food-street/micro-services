package com.imthath.food_street.court_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourtServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.3.0");

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void createCourt() {
		given()
				.contentType("application/json")
				.body("{\"name\": \"Test Court\", \"location\": \"Test Location\", \"imageUrl\": \"http://example.com/image.jpg\"}")
				.post("/court")
				.then()
				.statusCode(201)
				.body("id", notNullValue())
				.body("name", equalTo("Test Court"))
				.body("location", equalTo("Test Location"))
				.body("imageUrl", equalTo("http://example.com/image.jpg"));
	}

	@Test
	void getAllCourts() {
		createCourt();
		createCourt();
		given()
				.get("/court")
				.then()
				.statusCode(200)
				.body("size()", greaterThanOrEqualTo(2));
	}

	@Test
	void getCourtById() {
		var courtId = createSampleCourt();

		given()
				.get("/court/{id}", courtId)
				.then()
				.statusCode(200)
				.body("id", equalTo(courtId))
				.body("name", equalTo("Test Court"))
				.body("location", equalTo("Test Location"))
				.body("imageUrl", equalTo("http://example.com/image.jpg"));
	}

	@Test
	void updateCourt() {
		var courtId = createSampleCourt();

		given()
				.contentType("application/json")
				.body("{\"name\": \"Updated Court\", \"location\": \"Updated Location\", \"imageUrl\": \"http://example.com/updated_image.jpg\"}")
				.put("/court/{id}", courtId)
				.then()
				.statusCode(200)
				.body("id", equalTo(courtId))
				.body("name", equalTo("Updated Court"))
				.body("location", equalTo("Updated Location"))
				.body("imageUrl", equalTo("http://example.com/updated_image.jpg"));
	}

	@Test
	void deleteCourt() {
		var courtId = createSampleCourt();

		given()
				.delete("/court/{id}", courtId)
				.then()
				.statusCode(204);

		given()
				.get("/court/{id}", courtId)
				.then()
				.statusCode(901);
	}

	private Integer createSampleCourt() {
		return given()
				.contentType("application/json")
				.body("{\"name\": \"Test Court\", \"location\": \"Test Location\", \"imageUrl\": \"http://example.com/image.jpg\"}")
				.post("/court")
				.then()
				.statusCode(201)
				.extract()
				.path("id");
	}
}