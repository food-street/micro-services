package com.imthath.food_street.restaurant_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${court.service.url}")
    private String courtServiceUrl;

    @Bean
    public CourtClient courtClient() {
        return HttpServiceProxyFactory.builderFor(
                    RestClientAdapter.create(
                        RestClient.builder()
                            .baseUrl(courtServiceUrl)
                            .build()
                    )
                )
                .build()
                .createClient(CourtClient.class);
    }
} 