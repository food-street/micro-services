package com.imthath.food_street.menu_service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RestaurantServiceClient {
    
    static void stubRestaurantExists(long restaurantId) {
        stubFor(get(urlEqualTo("/restaurant/check?id=" + restaurantId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                        .withStatus(200)));
    }

    static void stubRestaurantNotExists(long restaurantId) {
        stubFor(get(urlEqualTo("/restaurant/check?id=" + restaurantId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")
                        .withStatus(200)));
    }

    static void stubRestaurantServiceError(long restaurantId) {
        stubFor(get(urlEqualTo("/restaurant/check?id=" + restaurantId))
                .willReturn(aResponse()
                        .withStatus(500)));
    }
}
