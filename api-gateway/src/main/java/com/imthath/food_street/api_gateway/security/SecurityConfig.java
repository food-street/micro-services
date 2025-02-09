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

    private static final String[] PUBLIC_ENDPOINTS = {"/fallbackRoute", "/api-docs", "/auth/**", "/actuator/**"};

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("setting up bean for security config");
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/otp/validate").hasRole("ADMIN")
                        .requestMatchers("/user/{id}/**").access((authentication, context) -> {
                            String id = context.getVariables().get("id");
                            String userId = (String) authentication.get().getPrincipal();
                            boolean hasUserRole = authentication.get().getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("USER"));
                            return new AuthorizationDecision(id.equals(userId) && hasUserRole);
                        })
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}