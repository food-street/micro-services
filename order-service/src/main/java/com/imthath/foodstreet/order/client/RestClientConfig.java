package com.imthath.foodstreet.order.client;

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

    @Value("${cart.service.url}")
    private String cartServiceUrl;

    @Bean
    public RestaurantClient restaurantClient() {
        return HttpServiceProxyFactory.builderFor(
                RestClientAdapter.create(
                        RestClient.builder()
                                .baseUrl(restaurantServiceUrl)
                                .build()
                )
        ).build().createClient(RestaurantClient.class);
    }

    @Bean
    public CartClient cartClient() {
        return HttpServiceProxyFactory.builderFor(
                RestClientAdapter.create(
                        RestClient.builder()
                                .baseUrl(cartServiceUrl)
                                .build()
                )
        ).build().createClient(CartClient.class);
    }
}
