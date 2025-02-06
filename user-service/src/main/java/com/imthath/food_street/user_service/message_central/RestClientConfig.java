package com.imthath.food_street.user_service.message_central;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class RestClientConfig {
    final String BASE_URL = "https://cpaas.messagecentral.com/verification/v3";

    @Bean
    TokenClient authClient() {
        return createClient(TokenClient.class);
    }

    @Bean
    OtpClient otpClient() {
        return createClient(OtpClient.class);
    }

    private <T> T createClient(Class<T> clazz) {
        return HttpServiceProxyFactory.builderFor(
                    RestClientAdapter.create(
                        RestClient.builder()
                            .baseUrl(BASE_URL)
                            .build()
                    )
                )
                .build()
                .createClient(clazz);
    }
}
