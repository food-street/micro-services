package com.imthath.food_street.restaurant_service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class CourtServiceClient {
    static void stubCourtExists(Long courtId) {
        stubFor(get(urlEqualTo("/court/check?id=" + courtId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                        .withStatus(200)));
    }

    static void stubCourtNotExists(Long courtId) {
        stubFor(get(urlEqualTo("/court/check?id=" + courtId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")
                        .withStatus(200)));
    }

    static void stubCourtServiceError(Long courtId) {
        stubFor(get(urlEqualTo("/court/check?id=" + courtId))
                .willReturn(aResponse()
                        .withStatus(500)));
    }
} 