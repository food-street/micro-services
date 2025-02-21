package com.imthath.food_street.menu_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class MenuCategoryTests {

    @LocalServerPort
    private int port;

    private final long VALID_RESTAURANT_ID = 123;
    private final long INVALID_RESTAURANT_ID = 999;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        
        RestaurantServiceClient.stubRestaurantExists(VALID_RESTAURANT_ID);
        RestaurantServiceClient.stubRestaurantNotExists(INVALID_RESTAURANT_ID);
    }

    @Test
    void createCategory() {
        given()
            .contentType("application/json")
            .body("""
                {
                    "name": "Test Category",
                    "description": "Test Category Description",
                    "restaurantId": 123,
                    "displayOrder": 1,
                    "isAvailable": true
                }
                """)
            .post("/menu/{restaurantId}/categories", VALID_RESTAURANT_ID)
            .then()
            .statusCode(201)
            .body("name", equalTo("Test Category"))
            .body("description", equalTo("Test Category Description"));
    }

    @Test
    void updateCategory() {
        var categoryId = createTestCategory();

        given()
            .contentType("application/json")
            .body("""
                {
                    "name": "Updated Category",
                    "description": "Updated Category Description"
                }
                """)
            .put("/menu/{restaurantId}/categories/{categoryId}", VALID_RESTAURANT_ID, categoryId)
            .then()
            .statusCode(200)
            .body("name", equalTo("Updated Category"))
            .body("description", equalTo("Updated Category Description"));
    }

    @Test
    void deleteCategory() {
        var categoryId = createTestCategory();

        given()
            .delete("/menu/{restaurantId}/categories/{categoryId}", VALID_RESTAURANT_ID, categoryId)
            .then()
            .statusCode(204);
    }

    @Test
    void createCategoryForInvalidRestaurant() {
        given()
            .contentType("application/json")
            .body("""
                {
                    "name": "Test Category",
                    "description": "Test Category Description"
                }
                """)
            .post("/menu/{restaurantId}/categories", INVALID_RESTAURANT_ID)
            .then()
            .statusCode(404);
    }

    private String createTestCategory() {
        return given()
            .contentType("application/json")
            .body("""
                {
                    "name": "Test Category",
                    "description": "Test Category Description"
                }
                """)
            .post("/menu/{restaurantId}/categories", VALID_RESTAURANT_ID)
            .then()
            .statusCode(201)
            .extract()
            .path("id");
    }
}
