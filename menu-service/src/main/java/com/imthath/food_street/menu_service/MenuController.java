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
    public List<CategoryResponse> getMenuByRestaurant(@PathVariable long restaurantId) {
        return menuService.getMenuByRestaurant(restaurantId);
    }

    @GetMapping("/{restaurantId}/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<Item> searchInRestaurant(
            @PathVariable long restaurantId,
            @RequestParam String keyword,
            Pageable pageable) {
        return menuService.searchMenuInRestaurant(restaurantId, keyword, pageable);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<Item> searchInRestaurants(
            @RequestParam List<Long> restaurantIds,
            @RequestParam String keyword,
            Pageable pageable) {
        return menuService.searchMenuInRestaurants(restaurantIds, keyword, pageable);
    }

    @PostMapping("/{restaurantId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(
            @PathVariable long restaurantId,
            @Valid @RequestBody CreateItemRequest request) {
        return menuService.createItem(restaurantId, request);
    }

    @PutMapping("/{restaurantId}/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item updateItem(
            @PathVariable long restaurantId,
            @PathVariable String itemId,
            @Valid @RequestBody UpdateItemRequest request) {
        return menuService.updateItem(restaurantId, itemId, request);
    }

    @DeleteMapping("/{restaurantId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(
            @PathVariable long restaurantId,
            @PathVariable String itemId) {
        menuService.deleteItem(restaurantId, itemId);
    }

    @PostMapping("/{restaurantId}/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(
            @PathVariable long restaurantId,
            @Valid @RequestBody CreateCategoryRequest request) {
        return menuService.createCategory(restaurantId, request);
    }

    @PutMapping("/{restaurantId}/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Category updateCategory(
            @PathVariable long restaurantId,
            @PathVariable String categoryId,
            @Valid @RequestBody UpdateCategoryRequest request) {
        return menuService.updateCategory(restaurantId, categoryId, request);
    }

    @DeleteMapping("/{restaurantId}/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable long restaurantId,
            @PathVariable String categoryId) {
        menuService.deleteCategory(restaurantId, categoryId);
    }
}
