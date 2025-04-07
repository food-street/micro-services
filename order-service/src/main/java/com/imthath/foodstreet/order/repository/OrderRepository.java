package com.imthath.foodstreet.order.repository;

import com.imthath.foodstreet.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findByFoodCourtId(String foodCourtId);
    List<Order> findByRestaurantOrdersRestaurantId(String restaurantId);
} 