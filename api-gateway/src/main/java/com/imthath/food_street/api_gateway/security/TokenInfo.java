package com.imthath.food_street.api_gateway.security;

import java.util.Optional;
import java.util.Set;

public record TokenInfo(String userId, Set<String> roles, Optional<String> entityId) {}