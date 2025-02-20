package com.imthath.food_street.restaurant_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

@Component
public interface CourtClient {
    @GetExchange("/court/check")
    boolean checkCourtExists(@RequestParam Long id);
} 