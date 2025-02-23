package com.imthath.food_street.menu_service;

import com.imthath.food_street.menu_service.client.RestaurantClient;
import com.imthath.food_street.menu_service.dto.*;
import com.imthath.food_street.menu_service.model.Category;
import com.imthath.food_street.menu_service.model.Item;
import com.imthath.food_street.menu_service.repo.CategoryRepository;
import com.imthath.food_street.menu_service.repo.ItemRepository;
import com.imthath.utils.guardrail.GenericException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.imthath.food_street.menu_service.MenuError.*;

@Service
@Slf4j
public class MenuService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantClient restaurantClient;

    public MenuService(ItemRepository itemRepository, CategoryRepository categoryRepository, RestaurantClient restaurantClient) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.restaurantClient = restaurantClient;
    }

    // Get menu of a restaurant - categories with nested items
    public List<CategoryResponse> getMenuByRestaurant(long restaurantId) {
        verifyRestaurantExists(restaurantId);
        List<Category> categories = categoryRepository.findByRestaurantIdAndIsAvailableOrderByDisplayOrder(restaurantId, true);
        List<CategoryResponse> menu = new ArrayList<>();

        for (Category category : categories) {
            List<Item> items = itemRepository.findByRestaurantIdAndCategoryIdAndIsAvailableOrderByDisplayOrder(restaurantId, category.getId(), true);
            if (!items.isEmpty()) {
                menu.add(CategoryResponse.from(category, items));
            }
        }

        return menu;
    }

    // Search within a restaurant (paginated)
    public Page<Item> searchMenuInRestaurant(long restaurantId, String keyword, Pageable pageable) {
        verifyRestaurantExists(restaurantId);
        return itemRepository.searchByRestaurant(restaurantId, keyword, pageable);
    }

    // Search across multiple restaurants (paginated)
    public Page<Item> searchMenuInRestaurants(List<Long> restaurantIds, String keyword, Pageable pageable) {
        return itemRepository.searchByRestaurantIds(restaurantIds, keyword, pageable);
    }

    public Item createItem(long restaurantId, CreateItemRequest request) {
        if (restaurantId != request.restaurantId()) {
            throw new GenericException(RESTAURANT_MISMATCH);
        }
        verifyRestaurantExists(restaurantId);
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

    public Item updateItem(long restaurantId, String itemId, UpdateItemRequest request) {
        verifyRestaurantExists(restaurantId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GenericException(ITEM_NOT_FOUND));

        if (restaurantId != item.getRestaurantId()) {
            throw new GenericException(RESTAURANT_MISMATCH);
        }

        if (request.name() != null) item.setName(request.name());
        if (request.description() != null) item.setDescription(request.description());
        if (request.price() != null) item.setPrice(request.price());
        if (request.imageUrl() != null) item.setImageUrl(request.imageUrl());
        if (request.displayOrder() != null) item.setDisplayOrder(request.displayOrder());
        if (request.isAvailable() != null) item.setAvailable(request.isAvailable());

        return itemRepository.save(item);
    }

    public void deleteItem(long restaurantId, String itemId) {
        verifyRestaurantExists(restaurantId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GenericException(ITEM_NOT_FOUND));

        if (restaurantId != item.getRestaurantId()) {
            throw new GenericException(RESTAURANT_MISMATCH);
        }
        itemRepository.deleteById(itemId);
    }

    public Category createCategory(long restaurantId, CreateCategoryRequest request) {
        if (restaurantId != request.restaurantId()) {
            throw new GenericException(RESTAURANT_MISMATCH);
        }
        verifyRestaurantExists(restaurantId);
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

    public Category updateCategory(long restaurantId, String categoryId, UpdateCategoryRequest request) {
        verifyRestaurantExists(restaurantId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GenericException(CATEGORY_NOT_FOUND));

        if (restaurantId != category.getRestaurantId()) {
            throw new GenericException(RESTAURANT_MISMATCH);
        }

        if (request.name() != null) category.setName(request.name());
        if (request.description() != null) category.setDescription(request.description());
        if (request.imageUrl() != null) category.setImageUrl(request.imageUrl());
        if (request.displayOrder() != null) category.setDisplayOrder(request.displayOrder());
        if (request.isAvailable() != null) category.setAvailable(request.isAvailable());

        return categoryRepository.save(category);
    }

    public void deleteCategory(long restaurantId, String categoryId) {
        verifyRestaurantExists(restaurantId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GenericException(CATEGORY_NOT_FOUND));

        if (restaurantId != category.getRestaurantId()) {
            throw new GenericException(RESTAURANT_MISMATCH);
        }
        categoryRepository.deleteById(categoryId);
    }

    private void verifyRestaurantExists(long restaurantId) throws GenericException {
        boolean exists = false;
        try {
            exists = restaurantClient.checkRestaurantExists(restaurantId);
        } catch (Exception e) {
            log.error("Failed to check restaurant with ID {}", restaurantId, e);
        }
        if (!exists) {
            log.warn("Restaurant with ID {} not found", restaurantId);
            throw new GenericException(RESTAURANT_NOT_FOUND);
        }
    }
}

