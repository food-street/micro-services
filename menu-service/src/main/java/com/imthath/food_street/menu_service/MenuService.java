package com.imthath.food_street.menu_service;

import com.imthath.food_street.menu_service.model.Category;
import com.imthath.food_street.menu_service.model.MenuItem;
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

    // 1. Get menu of a restaurant - categories with nested items
    public Map<Category, List<MenuItem>> getMenuByRestaurant(String restaurantId) {
        List<Category> categories = categoryRepository.findByRestaurantId(restaurantId);
        Map<Category, List<MenuItem>> menu = new HashMap<>();

        for (Category category : categories) {
            List<MenuItem> items = itemRepository.findByRestaurantId(restaurantId, Pageable.unpaged()).getContent();
            menu.put(category, items);
        }

        return menu;
    }

    // 2. Search within a restaurant (paginated)
    public Page<MenuItem> searchMenuInRestaurant(String restaurantId, String keyword, Pageable pageable) {
        return itemRepository.searchByRestaurant(restaurantId, keyword, pageable);
    }

    // 3. Search across multiple restaurants (paginated)
    public Page<MenuItem> searchMenuInRestaurants(List<String> restaurantIds, String keyword, Pageable pageable) {
        return itemRepository.searchByRestaurantIds(restaurantIds, keyword, pageable);
    }
}

