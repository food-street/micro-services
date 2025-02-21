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
class MenuItemTests {

    @LocalServerPort
    private int port;

    private final String VALID_RESTAURANT_ID = "rest123";
    private final String INVALID_RESTAURANT_ID = "rest999";
    private String categoryId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        
        RestaurantServiceClient.stubRestaurantExists(VALID_RESTAURANT_ID);
        RestaurantServiceClient.stubRestaurantNotExists(INVALID_RESTAURANT_ID);
        
        // Create a test category for items
        categoryId = createTestCategory();
    }

    @Test
    void createItem() {
        given()
            .contentType("application/json")
            .body(String.format("""
                {
                    "name": "Test Item",
                    "description": "Test Description",
                    "price": 9.99,
                    "categoryId": "%s",
                    "imageUrl": "http://example.com/image.jpg"
                }
                """, categoryId))
            .post("/menu/{restaurantId}/items", VALID_RESTAURANT_ID)
            .then()
            .statusCode(201)
            .body("name", equalTo("Test Item"))
            .body("description", equalTo("Test Description"))
            .body("price", equalTo(9.99f))
            .body("categoryId", equalTo(categoryId));
    }

    @Test
    void updateItem() {
        var itemId = createTestItem();

        given()
            .contentType("application/json")
            .body(String.format("""
                {
                    "name": "Updated Item",
                    "description": "Updated Description",
                    "price": 19.99,
                    "categoryId": "%s",
                    "imageUrl": "http://example.com/updated.jpg"
                }
                """, categoryId))
            .put("/menu/{restaurantId}/items/{itemId}", VALID_RESTAURANT_ID, itemId)
            .then()
            .statusCode(200)
            .body("name", equalTo("Updated Item"))
            .body("description", equalTo("Updated Description"))
            .body("price", equalTo(19.99f));
    }

    @Test
    void deleteItem() {
        var itemId = createTestItem();

        given()
            .delete("/menu/{restaurantId}/items/{itemId}", VALID_RESTAURANT_ID, itemId)
            .then()
            .statusCode(204);
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

    private String createTestItem() {
        return given()
            .contentType("application/json")
            .body(String.format("""
                {
                    "name": "Test Item",
                    "description": "Test Description",
                    "price": 9.99,
                    "categoryId": "%s",
                    "imageUrl": "http://example.com/image.jpg"
                }
                """, categoryId))
            .post("/menu/{restaurantId}/items", VALID_RESTAURANT_ID)
            .then()
            .statusCode(201)
            .extract()
            .path("id");
    }
}
