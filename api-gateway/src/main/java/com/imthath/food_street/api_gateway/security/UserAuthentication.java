package com.imthath.food_street.api_gateway.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    static final String ROLE_PREFIX = "ROLE_";

    final String entityId;

    public UserAuthentication(String id, Set<String> roles, String entityId) {
        super(
            id,
            null,
            roles.stream().map(r -> new SimpleGrantedAuthority(ROLE_PREFIX + r)).toList()
        );
        this.entityId = entityId;
    }
}
