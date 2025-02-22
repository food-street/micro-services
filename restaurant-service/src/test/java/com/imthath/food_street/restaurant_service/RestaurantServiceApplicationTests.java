package com.imthath.food_street.restaurant_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static com.imthath.food_street.restaurant_service.error.RestaurantError.*;

@SuppressWarnings("TrailingWhitespacesInTextBlock")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class RestaurantServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"));

    @LocalServerPort
    private int port;

    private final Long VALID_COURT_ID = 1L;
    private final Long INVALID_COURT_ID = 999L;
    private final Long ERROR_COURT_ID = 500L;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        
        // Setup court service stubs
        CourtServiceClient.stubCourtExists(VALID_COURT_ID);
        CourtServiceClient.stubCourtNotExists(INVALID_COURT_ID);
        CourtServiceClient.stubCourtServiceError(ERROR_COURT_ID);
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
                        "courtId": """ + VALID_COURT_ID + """
                    }
                    """)
                .post("/restaurant")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Test Restaurant"))
                .body("description", equalTo("Test Description"))
                .body("imageUrl", equalTo("http://example.com/image.jpg"))
                .body("courtId", equalTo(VALID_COURT_ID.intValue()));
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
                .body("courtId", equalTo(VALID_COURT_ID.intValue()));
    }

    @Test
    void getRestaurantsByCourtId() {
        createSampleRestaurant();
        createSampleRestaurant();

        given()
                .get("/restaurant?courtId={courtId}", VALID_COURT_ID)
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
                        "courtId": """ + VALID_COURT_ID + """
                    }
                    """)
                .put("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(200)
                .body("id", equalTo(restaurantId))
                .body("name", equalTo("Updated Restaurant"))
                .body("description", equalTo("Updated Description"))
                .body("imageUrl", equalTo("http://example.com/updated_image.jpg"))
                .body("courtId", equalTo(VALID_COURT_ID.intValue()));
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
                .statusCode(RESTAURANT_NOT_FOUND.getCode());
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
                        "courtId": """ + INVALID_COURT_ID + """
                    }
                    """)
                .post("/restaurant")
                .then()
                .statusCode(COURT_NOT_FOUND.getCode());
    }

    @Test
    void createRestaurantWithoutCourtId() {
        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Test Restaurant",
                        "description": "Test Description",
                        "imageUrl": "http://example.com/image.jpg"
                    }
                    """)
                .post("/restaurant")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Test Restaurant"))
                .body("description", equalTo("Test Description"))
                .body("imageUrl", equalTo("http://example.com/image.jpg"))
                .body("courtId", nullValue());
    }

    @Test
    void updateRestaurantRemoveCourtId() {
        var restaurantId = createSampleRestaurant();

        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Updated Restaurant",
                        "description": "Updated Description",
                        "imageUrl": "http://example.com/updated_image.jpg"
                    }
                    """)
                .put("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(COURT_ID_MISMATCH.getCode());
    }

    @Test
    void createRestaurantWithCourtServiceError() {
        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Test Restaurant",
                        "description": "Test Description",
                        "imageUrl": "http://example.com/image.jpg",
                        "courtId": """ + ERROR_COURT_ID + """
                    }
                    """)
                .post("/restaurant")
                .then()
                .statusCode(COURT_NOT_FOUND.getCode());
    }

    @Test
    void updateRestaurantWithMatchingCourtId() {
        var restaurantId = createSampleRestaurant();

        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Updated Restaurant",
                        "description": "Updated Description",
                        "imageUrl": "http://example.com/updated_image.jpg",
                        "courtId": """ + VALID_COURT_ID + """
                    }
                    """)
                .put("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(200)
                .body("id", equalTo(restaurantId))
                .body("name", equalTo("Updated Restaurant"))
                .body("description", equalTo("Updated Description"))
                .body("imageUrl", equalTo("http://example.com/updated_image.jpg"))
                .body("courtId", equalTo(VALID_COURT_ID.intValue()));
    }

    @Test
    void updateRestaurantWithDifferentCourtId() {
        var restaurantId = createSampleRestaurant();

        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Updated Restaurant",
                        "description": "Updated Description",
                        "imageUrl": "http://example.com/updated_image.jpg",
                        "courtId": """ + INVALID_COURT_ID + """
                    }
                    """)
                .put("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(COURT_ID_MISMATCH.getCode());
    }

    @Test
    void updateRestaurantWithNullCourtIdWhenNoneExists() {
        var restaurantId = createRestaurantWithoutCourt();

        // First, update to remove the courtId
        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Updated Restaurant",
                        "description": "Updated Description",
                        "imageUrl": "http://example.com/updated_image.jpg",
                        "courtId": null
                    }
                    """)
                .put("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(200)
                .body("id", equalTo(restaurantId))
                .body("name", equalTo("Updated Restaurant"))
                .body("description", equalTo("Updated Description"))
                .body("imageUrl", equalTo("http://example.com/updated_image.jpg"))
                .body("courtId", nullValue());
    }

    @Test
    void updateRestaurantWithNullCourtIdWhenExists() {
        var restaurantId = createSampleRestaurant();

        given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Updated Restaurant",
                        "description": "Updated Description",
                        "imageUrl": "http://example.com/updated_image.jpg",
                        "courtId": null
                    }
                    """)
                .put("/restaurant/{id}", restaurantId)
                .then()
                .statusCode(COURT_ID_MISMATCH.getCode());
    }

    private Integer createSampleRestaurant() {
        return given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Test Restaurant",
                        "description": "Test Description",
                        "imageUrl": "http://example.com/image.jpg",
                        "courtId": """ + VALID_COURT_ID + """
                    }
                    """)
                .post("/restaurant")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private Integer createRestaurantWithoutCourt() {
        return given()
                .contentType("application/json")
                .body("""
                    {
                        "name": "Test Restaurant",
                        "description": "Test Description",
                        "imageUrl": "http://example.com/image.jpg"
                    }
                    """)
                .post("/restaurant")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }
} 