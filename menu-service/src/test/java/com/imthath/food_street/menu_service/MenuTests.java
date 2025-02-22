package com.imthath.food_street.menu_service;

import static io.restassured.RestAssured.given;

public class MenuTests {
    final long VALID_RESTAURANT_ID = 123;
    final long INVALID_RESTAURANT_ID = 999;

    final int RESTAURANT_NOT_FOUND = 901;
    final int RESTAURANT_MISMATCH = 902;

    String createTestCategory() {
        return given()
                .contentType("application/json")
                .body(validCategoryRequestBody())
                .post("/menu/{restaurantId}/categories", VALID_RESTAURANT_ID)
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    String createTestItem(String categoryId) {
        return given()
                .contentType("application/json")
                .body(String.format("""
                {
                    "name": "Test Item",
                    "description": "Test Description",
                    "price": 9.99,
                    "categoryId": "%s",
                    "restaurantId": %d,
                    "displayOrder": 1,
                    "imageUrl": "http://example.com/image.jpg"
                }
                """, categoryId, VALID_RESTAURANT_ID))
                .post("/menu/{restaurantId}/items", VALID_RESTAURANT_ID)
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    String validCategoryRequestBody() {
        return String.format("""
                {
                    "name": "Test Category",
                    "description": "Test Category Description",
                    "restaurantId": %d,
                    "displayOrder": 1,
                    "isAvailable": true
                }
                """, VALID_RESTAURANT_ID);
    }
}
