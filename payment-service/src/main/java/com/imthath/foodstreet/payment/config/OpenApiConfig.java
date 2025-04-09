package com.imthath.foodstreet.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server");

        Contact contact = new Contact()
                .name("Food Street Team")
                .email("support@foodstreet.com")
                .url("https://www.foodstreet.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Food Street Payment API")
                .version("1.0.0")
                .contact(contact)
                .description("API documentation for the Food Street Payment Service")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
} 