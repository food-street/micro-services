package com.imthath.food_street.menu_service.repo;

import com.imthath.food_street.menu_service.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {

    Page<Item> findByRestaurantId(String restaurantId, Pageable pageable);

    @Query("{ 'restaurantId': ?0, '$or': [ { 'name': { $regex: ?1, $options: 'i' } }, { 'description': { $regex: ?1, $options: 'i' } } ] }")
    Page<Item> searchByRestaurant(String restaurantId, String keyword, Pageable pageable);

    @Query("{ 'restaurantId': { $in: ?0 }, '$or': [ { 'name': { $regex: ?1, $options: 'i' } }, { 'description': { $regex: ?1, $options: 'i' } } ] }")
    Page<Item> searchByRestaurantIds(List<String> restaurantIds, String keyword, Pageable pageable);
}

