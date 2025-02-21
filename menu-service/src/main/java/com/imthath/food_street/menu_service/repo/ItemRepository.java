package com.imthath.food_street.menu_service.repo;

import com.imthath.food_street.menu_service.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ItemRepository extends MongoRepository<MenuItem, String> {

    // 1. Get all menu items for a restaurant (paginated)
    Page<MenuItem> findByRestaurantId(String restaurantId, Pageable pageable);

    // 2. Search within a restaurant by name or description
    @Query("{ 'restaurantId': ?0, '$or': [ { 'name': { $regex: ?1, $options: 'i' } }, { 'description': { $regex: ?1, $options: 'i' } } ] }")
    Page<MenuItem> searchByRestaurant(String restaurantId, String keyword, Pageable pageable);

    // 3. Search across multiple restaurants by IDs
    @Query("{ 'restaurantId': { $in: ?0 }, '$or': [ { 'name': { $regex: ?1, $options: 'i' } }, { 'description': { $regex: ?1, $options: 'i' } } ] }")
    Page<MenuItem> searchByRestaurantIds(List<String> restaurantIds, String keyword, Pageable pageable);
}

