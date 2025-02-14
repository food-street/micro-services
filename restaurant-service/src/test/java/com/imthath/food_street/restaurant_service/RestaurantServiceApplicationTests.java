package com.imthath.food_street.restaurant_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.26"));

    @LocalServerPort
    private int port;

    private final Integer sampleCourtId = 1; // We'll assume this court exists for testing

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void createRestaurant() {
        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Test Restaurant",
                        "description": "Test Description",
                        "imageUrl": "http://example.com/image.jpg",
                        "courtId": """ + sampleCourtId + """
                    }
                    """)
                .post("/restaurant")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Test Restaurant"))
                .body("description", equalTo("Test Description"))
                .body("imageUrl", equalTo("http://example.com/image.jpg"))
                .body("courtId", equalTo(sampleCourtId));
    }

    @Test
    void getAllRestaurants() {
        createSampleRestaurant();
        createSampleRestaurant();
        
        given()
                .get("/restaurant")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    void getRestaurantById() {
        var restaurantId = createSampleRestaurant();

        given()
                .get("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(200)
                .body("id", equalTo(restaurantId))
                .body("name", equalTo("Test Restaurant"))
                .body("description", equalTo("Test Description"))
                .body("imageUrl", equalTo("http://example.com/image.jpg"))
                .body("courtId", equalTo(sampleCourtId));
    }

    @Test
    void getRestaurantsByCourtId() {
        createSampleRestaurant();
        createSampleRestaurant();

        given()
                .get("/restaurant/court/{courtId}", sampleCourtId)
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    void updateRestaurant() {
        var restaurantId = createSampleRestaurant();

        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Updated Restaurant",
                        "description": "Updated Description",
                        "imageUrl": "http://example.com/updated_image.jpg",
                        "courtId": """ + sampleCourtId + """
                    }
                    """)
                .put("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(200)
                .body("id", equalTo(restaurantId))
                .body("name", equalTo("Updated Restaurant"))
                .body("description", equalTo("Updated Description"))
                .body("imageUrl", equalTo("http://example.com/updated_image.jpg"))
                .body("courtId", equalTo(sampleCourtId));
    }

    @Test
    void deleteRestaurant() {
        var restaurantId = createSampleRestaurant();

        given()
                .delete("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(204);

        given()
                .get("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(902); // RESTAURANT_NOT_FOUND error code
    }

    @Test
    void createRestaurantWithInvalidCourt() {
        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Test Restaurant",
                        "description": "Test Description",
                        "imageUrl": "http://example.com/image.jpg",
                        "courtId": 99999
                    }
                    """)
                .post("/restaurant")
                .then()
                .statusCode(901); // COURT_NOT_FOUND error code
    }

    private Integer createSampleRestaurant() {
        return given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Test Restaurant",
                        "description": "Test Description",
                        "imageUrl": "http://example.com/image.jpg",
                        "courtId": """ + sampleCourtId + """
                    }
                    """)
                .post("/restaurant")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }
} 