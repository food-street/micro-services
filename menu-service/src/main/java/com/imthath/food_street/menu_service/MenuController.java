package com.imthath.food_street.menu_service;

import com.imthath.food_street.menu_service.model.Category;
import com.imthath.food_street.menu_service.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // 1. Get menu of a restaurant
    @GetMapping("/{restaurantId}")
    public ResponseEntity<Map<Category, List<MenuItem>>> getMenuByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(menuService.getMenuByRestaurant(restaurantId));
    }

    // 2. Search in a restaurant
    @GetMapping("/{restaurantId}/search")
    public ResponseEntity<Page<MenuItem>> searchInRestaurant(
            @PathVariable String restaurantId,
            @RequestParam String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(menuService.searchMenuInRestaurant(restaurantId, keyword, pageable));
    }

    // 3. Search across multiple restaurants
    @GetMapping("/search")
    public ResponseEntity<Page<MenuItem>> searchInRestaurants(
            @RequestParam List<String> restaurantIds,
            @RequestParam String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(menuService.searchMenuInRestaurants(restaurantIds, keyword, pageable));
    }
}
