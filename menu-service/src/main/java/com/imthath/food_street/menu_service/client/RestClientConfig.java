package com.imthath.food_street.menu_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${restaurant.service.url}")
    private String restaurantServiceUrl;

    @Bean
    public RestaurantClient restaurantClient() {
        return HttpServiceProxyFactory.builderFor(
                    RestClientAdapter.create(
                        RestClient.builder()
                            .baseUrl(restaurantServiceUrl)
                            .build()
                    )
                )
                .build()
                .createClient(RestaurantClient.class);
    }
}
