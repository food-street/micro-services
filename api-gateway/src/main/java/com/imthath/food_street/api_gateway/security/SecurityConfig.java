package com.imthath.food_street.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;

@Configuration
public class SecurityConfig  {
    
    @Autowired
    SecurityFilter securityFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/fallbackRoute",
            "/api-docs",
            "/auth/send-otp", "/auth/validate-otp",
            "/actuator/**"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("setting up bean for security config");
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/court/{id}").access((authentication, context) -> {
                            String id = context.getVariables().get("id");
                            UserAuthentication userAuth = (UserAuthentication) authentication.get();
                            return new AuthorizationDecision(
                                id.equals(userAuth.entityId) && context.getRequest().isUserInRole("FC_ADMIN")
                            );
                        })
                        .requestMatchers("/otp/validate").hasRole("R_ADMIN")
                        .requestMatchers("/user/{id}/**").access((authentication, context) -> {
                            String id = context.getVariables().get("id");
                            String userId = (String) authentication.get().getPrincipal();
                            System.out.println("Checking user id matching in auth context and request path");
                            return new AuthorizationDecision(
                                id.equals(userId) && context.getRequest().isUserInRole("USER")
                            );
                        })
                        .requestMatchers("/user/**", "/court").hasRole("APP_ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}