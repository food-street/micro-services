package com.imthath.food_street.user_service.security;

record OtpResponse(String identifier, String maskedUserName) {}

record JwtResponse(String token) {}
