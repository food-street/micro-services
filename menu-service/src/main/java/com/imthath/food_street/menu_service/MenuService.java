package com.imthath.food_street.menu_service;

import com.imthath.food_street.menu_service.dto.*;
import com.imthath.food_street.menu_service.model.Category;
import com.imthath.food_street.menu_service.model.Item;
import com.imthath.food_street.menu_service.repo.CategoryRepository;
import com.imthath.food_street.menu_service.repo.ItemRepository;
import com.imthath.utils.guardrail.GenericException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

import static com.imthath.food_street.menu_service.MenuError.*;

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
        List<Category> categories = categoryRepository.findByRestaurantIdOrderByDisplayOrder(restaurantId);
        List<CategoryResponse> menu = new ArrayList<>();

        for (Category category : categories) {
            List<Item> items = itemRepository.findByRestaurantIdAndCategoryIdOrderByDisplayOrder(restaurantId, category.getId());
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

    public Item createItem(CreateItemRequest request) {
        Item item = Item.builder()
                .restaurantId(request.restaurantId())
                .categoryId(request.categoryId())
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .displayOrder(request.displayOrder())
                .isAvailable(request.isAvailable())
                .build();
        return itemRepository.save(item);
    }

    public Item updateItem(String itemId, UpdateItemRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        if (request.name() != null) item.setName(request.name());
        if (request.description() != null) item.setDescription(request.description());
        if (request.price() != null) item.setPrice(request.price());
        if (request.imageUrl() != null) item.setImageUrl(request.imageUrl());
        if (request.displayOrder() != null) item.setDisplayOrder(request.displayOrder());
        if (request.isAvailable() != null) item.setAvailable(request.isAvailable());

        return itemRepository.save(item);
    }

    public void deleteItem(String itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new GenericException(ITEM_NOT_FOUND);
        }
        itemRepository.deleteById(itemId);
    }

    public Category createCategory(CreateCategoryRequest request) {
        Category category = Category.builder()
                .restaurantId(request.restaurantId())
                .name(request.name())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .displayOrder(request.displayOrder())
                .isAvailable(request.isAvailable())
                .build();
        return categoryRepository.save(category);
    }

    public Category updateCategory(String categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GenericException(CATEGORY_NOT_FOUND));

        if (request.name() != null) category.setName(request.name());
        if (request.description() != null) category.setDescription(request.description());
        if (request.imageUrl() != null) category.setImageUrl(request.imageUrl());
        if (request.displayOrder() != null) category.setDisplayOrder(request.displayOrder());
        if (request.isAvailable() != null) category.setAvailable(request.isAvailable());

        return categoryRepository.save(category);
    }

    public void deleteCategory(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new GenericException(CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(categoryId);
    }
}

