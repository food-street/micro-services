package com.imthath.food_street.menu_service.repo;

import com.imthath.food_street.menu_service.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByRestaurantIdOrderByDisplayOrder(long restaurantId);
}
