package com.imthath.food_street.menu_service;

import com.imthath.food_street.menu_service.dto.CategoryResponse;
import com.imthath.food_street.menu_service.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getMenuByRestaurant(@PathVariable String restaurantId) {
        return menuService.getMenuByRestaurant(restaurantId);
    }

    @GetMapping("/{restaurantId}/search")
    public ResponseEntity<Page<Item>> searchInRestaurant(
            @PathVariable String restaurantId,
            @RequestParam String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(menuService.searchMenuInRestaurant(restaurantId, keyword, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Item>> searchInRestaurants(
            @RequestParam List<String> restaurantIds,
            @RequestParam String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(menuService.searchMenuInRestaurants(restaurantIds, keyword, pageable));
    }
}
