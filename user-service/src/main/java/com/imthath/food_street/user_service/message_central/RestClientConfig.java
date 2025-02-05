package com.imthath.food_street.user_service.message_central;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class AuthClientConfig {
    final String BASE_URL = "https://cpaas.messagecentral.com";

    @Bean
    TokenClient authClient() {
        return HttpServiceProxyFactory.builderFor(
                    RestClientAdapter.create(
                        RestClient.builder()
                            .baseUrl(BASE_URL)
                            .build()
                    )
                )
                .build()
                .createClient(TokenClient.class);
    }
}

@Configuration
class OtpClientConfig {
    final String BASE_URL = "https://cpaas.messagecentral.com";

    @Autowired
    TokenService tokenService;

    @Bean
    OtpClient otpClient() {
        return HttpServiceProxyFactory.builderFor(
                        RestClientAdapter.create(
                                RestClient.builder()
                                        .baseUrl(BASE_URL)
                                        .defaultHeader(tokenService.key, tokenService.generateAuthToken())
                                        .build()
                        )
                )
                .build()
                .createClient(OtpClient.class);
    }
}
