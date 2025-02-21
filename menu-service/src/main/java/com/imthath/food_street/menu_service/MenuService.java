package com.imthath.food_street.menu_service;

import com.imthath.food_street.menu_service.dto.CategoryResponse;
import com.imthath.food_street.menu_service.model.Category;
import com.imthath.food_street.menu_service.model.Item;
import com.imthath.food_street.menu_service.repo.CategoryRepository;
import com.imthath.food_street.menu_service.repo.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public MenuService(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    // Get menu of a restaurant - categories with nested items
    public List<CategoryResponse> getMenuByRestaurant(String restaurantId) {
        List<Category> categories = categoryRepository.findByRestaurantId(restaurantId);
        List<CategoryResponse> menu = new ArrayList<>();

        for (Category category : categories) {
            List<Item> items = itemRepository.findByRestaurantId(restaurantId, Pageable.unpaged()).getContent();
            menu.add(CategoryResponse.from(category, items));
        }

        return menu;
    }

    // Search within a restaurant (paginated)
    public Page<Item> searchMenuInRestaurant(String restaurantId, String keyword, Pageable pageable) {
        return itemRepository.searchByRestaurant(restaurantId, keyword, pageable);
    }

    // Search across multiple restaurants (paginated)
    public Page<Item> searchMenuInRestaurants(List<String> restaurantIds, String keyword, Pageable pageable) {
        return itemRepository.searchByRestaurantIds(restaurantIds, keyword, pageable);
    }
}

