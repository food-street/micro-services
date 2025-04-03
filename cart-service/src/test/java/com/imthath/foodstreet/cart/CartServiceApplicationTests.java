package com.imthath.foodstreet.cart;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static com.imthath.foodstreet.cart.error.CartError.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CartServiceApplicationTests {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.4.2"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @LocalServerPort
    private int port;

    private final String USER_ID = "test-user";
    private final String FOOD_COURT_ID = "test-court";
    private final String MENU_ITEM_ID = "test-item";
    private final String RESTAURANT_ID = "test-restaurant";
    private final String DIFFERENT_FOOD_COURT_ID = "different-court";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        // Clear cart at the start of each test
        given()
                .delete("/cart/{userId}", USER_ID);
    }

    @Test
    void getEmptyCart() {
        given()
                .get("/cart/{userId}", USER_ID)
                .then()
                .statusCode(CART_NOT_FOUND.getCode());
    }

    @Test
    void addItemToCart() {
        given()
                .contentType("application/json")
                .queryParam("foodCourtId", FOOD_COURT_ID)
                .body("""
                    {
                        "menuItemId": "%s",
                        "restaurantId": "%s",
                        "name": "Test Item",
                        "price": 10.99,
                        "quantity": 2
                    }
                    """.formatted(MENU_ITEM_ID, RESTAURANT_ID))
                .post("/cart/{userId}/items", USER_ID)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("userId", equalTo(USER_ID))
                .body("foodCourtId", equalTo(FOOD_COURT_ID))
                .body("items", hasSize(1))
                .body("items[0].menuItemId", equalTo(MENU_ITEM_ID))
                .body("items[0].restaurantId", equalTo(RESTAURANT_ID))
                .body("items[0].name", equalTo("Test Item"))
                .body("items[0].price", equalTo(10.99f))
                .body("items[0].quantity", equalTo(2))
                .body("total", equalTo(21.98f));
    }

    @Test
    void addItemWithMissingRequiredFields() {
        given()
                .contentType("application/json")
                .queryParam("foodCourtId", FOOD_COURT_ID)
                .body("""
                    {
                        "price": 10.99,
                        "quantity": 1
                    }
                    """)
                .post("/cart/{userId}/items", USER_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addItemWithInvalidPrice() {
        given()
                .contentType("application/json")
                .queryParam("foodCourtId", FOOD_COURT_ID)
                .body("""
                    {
                        "menuItemId": "%s",
                        "restaurantId": "%s",
                        "name": "Test Item",
                        "price": -10.99,
                        "quantity": 1
                    }
                    """.formatted(MENU_ITEM_ID, RESTAURANT_ID))
                .post("/cart/{userId}/items", USER_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addItemWithInvalidQuantity() {
        given()
                .contentType("application/json")
                .queryParam("foodCourtId", FOOD_COURT_ID)
                .body("""
                    {
                        "menuItemId": "%s",
                        "restaurantId": "%s",
                        "name": "Test Item",
                        "price": 10.99,
                        "quantity": 0
                    }
                    """.formatted(MENU_ITEM_ID, RESTAURANT_ID))
                .post("/cart/{userId}/items", USER_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addItemFromDifferentFoodCourt() {
        // Add first item
        addSampleItem();

        // Try to add item from different food court
        given()
                .contentType("application/json")
                .queryParam("foodCourtId", DIFFERENT_FOOD_COURT_ID)
                .body("""
                    {
                        "menuItemId": "different-item",
                        "restaurantId": "different-restaurant",
                        "name": "Different Item",
                        "price": 15.99,
                        "quantity": 1
                    }
                    """)
                .post("/cart/{userId}/items", USER_ID)
                .then()
                .statusCode(DIFFERENT_FOOD_COURT_ITEMS.getCode());
    }

    @Test
    void updateItemQuantity() {
        addSampleItem();

        given()
                .queryParam("quantity", 5)
                .put("/cart/{userId}/items/{menuItemId}", USER_ID, MENU_ITEM_ID)
                .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .body("items[0].quantity", equalTo(5))
                .body("total", equalTo(54.95f));
    }

    @Test
    void updateNonExistentItem() {
        given()
                .queryParam("quantity", 5)
                .put("/cart/{userId}/items/{menuItemId}", USER_ID, "non-existent")
                .then()
                .statusCode(CART_NOT_FOUND.getCode());
    }

    @Test
    void updateItemWithInvalidQuantity() {
        addSampleItem();

        given()
                .queryParam("quantity", -1)
                .put("/cart/{userId}/items/{menuItemId}", USER_ID, MENU_ITEM_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void removeItem() {
        addSampleItem();

        given()
                .delete("/cart/{userId}/items/{menuItemId}", USER_ID, MENU_ITEM_ID)
                .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .body("items", hasSize(0))
                .body("total", equalTo(0.0f));
    }

    @Test
    void removeNonExistentItem() {
        given()
                .delete("/cart/{userId}/items/{menuItemId}", USER_ID, "non-existent")
                .then()
                .statusCode(CART_NOT_FOUND.getCode());
    }

    @Test
    void clearCart() {
        addSampleItem();

        given()
                .delete("/cart/{userId}", USER_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Verify cart is empty
        given()
                .get("/cart/{userId}", USER_ID)
                .then()
                .statusCode(CART_NOT_FOUND.getCode());
    }

    private void addSampleItem() {
        given()
                .contentType("application/json")
                .queryParam("foodCourtId", FOOD_COURT_ID)
                .body("""
                    {
                        "menuItemId": "%s",
                        "restaurantId": "%s",
                        "name": "Test Item",
                        "price": 10.99,
                        "quantity": 2
                    }
                    """.formatted(MENU_ITEM_ID, RESTAURANT_ID))
                .post("/cart/{userId}/items", USER_ID)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }
}