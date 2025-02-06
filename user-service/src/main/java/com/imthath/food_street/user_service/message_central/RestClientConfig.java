package com.imthath.food_street.user_service.message_central;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class RestClientConfig {
    @Value("${mc.base.url}")
    String BASE_URL;

    @Bean
    TokenClient authClient() {
        return createClient(TokenClient.class);
    }

    @Bean
    OtpClient otpClient() {
        return createClient(OtpClient.class);
    }

    private <T> T createClient(Class<T> clazz) {
        var restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
        var adapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory
                .builderFor(adapter)
                .build()
                .createClient(clazz);
    }
}
