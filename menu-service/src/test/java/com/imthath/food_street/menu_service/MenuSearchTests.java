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
class MenuSearchTests {

    @LocalServerPort
    private int port;

    private final long VALID_RESTAURANT_ID = 123;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        
        // Setup restaurant stubs
        RestaurantServiceClient.stubRestaurantExists(VALID_RESTAURANT_ID);
        
        // Create test data
        createTestMenuItems();
    }

    @Test
    void searchInRestaurant() {
        given()
            .param("keyword", "burger")
            .param("page", 0)
            .param("size", 10)
            .get("/menu/{restaurantId}/search", VALID_RESTAURANT_ID)
            .then()
            .statusCode(200)
            .body("content", hasSize(greaterThan(0)))
            .body("content[0].name", containsStringIgnoringCase("burger"));
    }

    @Test
    void searchInMultipleRestaurants() {
        given()
            .param("restaurantIds", VALID_RESTAURANT_ID)
            .param("keyword", "pizza")
            .param("page", 0)
            .param("size", 10)
            .get("/menu/search")
            .then()
            .statusCode(200)
            .body("content", hasSize(greaterThan(0)))
            .body("content[0].name", containsStringIgnoringCase("pizza"));
    }

    private void createTestMenuItems() {
        // Create test categories and items using the API
        var categoryId = createTestCategory();
        createTestItem(categoryId, "Cheeseburger", "Delicious burger with cheese");
        createTestItem(categoryId, "Pepperoni Pizza", "Classic pizza with pepperoni");
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

    private void createTestItem(String categoryId, String name, String description) {
        given()
            .contentType("application/json")
            .body(String.format("""
                {
                    "name": "%s",
                    "description": "%s",
                    "price": 9.99,
                    "categoryId": "%s",
                    "imageUrl": "http://example.com/image.jpg"
                }
                """, name, description, categoryId))
            .post("/menu/{restaurantId}/items", VALID_RESTAURANT_ID)
            .then()
            .statusCode(201);
    }
}
