package com.imthath.food_street.api_gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenParser tokenParser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            var info = tokenParser.parseToken(token);
            // map roles to SingleGrantedAuthority
            var authorities = info.roles().stream().map(SimpleGrantedAuthority::new).toList();
            var auth = new UsernamePasswordAuthenticationToken(info.userId(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("Authentication successful for user: " + info.userId());
        } catch (Exception e) {
            System.out.println("Unauthorized access with malformed token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }
}