package com.imthath.food_street.menu_service;

import com.imthath.food_street.menu_service.dto.*;
import com.imthath.food_street.menu_service.model.Category;
import com.imthath.food_street.menu_service.model.Item;
import jakarta.validation.Valid;
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

    @PostMapping("/{restaurantId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@Valid @RequestBody CreateItemRequest request) {
        return menuService.createItem(request);
    }

    @PutMapping("/{restaurantId}/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item updateItem(
            @PathVariable String itemId,
            @Valid @RequestBody UpdateItemRequest request) {
        return menuService.updateItem(itemId, request);
    }

    @DeleteMapping("/{restaurantId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String itemId) {
        menuService.deleteItem(itemId);
    }

    @PostMapping("/{restaurantId}/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return menuService.createCategory(request);
    }

    @PutMapping("/{restaurantId}/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Category updateCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody UpdateCategoryRequest request) {
        return menuService.updateCategory(categoryId, request);
    }

    @DeleteMapping("/{restaurantId}/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable String categoryId) {
        menuService.deleteCategory(categoryId);
    }
}
