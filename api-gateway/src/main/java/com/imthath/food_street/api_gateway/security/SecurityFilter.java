package com.imthath.food_street.api_gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;

@SuppressWarnings("NullableProblems")
@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenParser tokenParser;

    @Autowired
    Environment environment;

    final String ROLE_PREFIX = "ROLE_";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.asList(environment.getActiveProfiles()).contains("local");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            var info = tokenParser.parseToken(token);
            var authorities = info.roles().stream().map(r -> new SimpleGrantedAuthority(ROLE_PREFIX + r)).toList();
            var auth = new UsernamePasswordAuthenticationToken(info.userId(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("Updated authentication context for user " + info.userId() + " with roles " + info.roles());
            if (info.entityId().isPresent()) {
                System.out.println("Authorized by entity " + info.entityId().get());
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("Unauthorized access with malformed token. Error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}