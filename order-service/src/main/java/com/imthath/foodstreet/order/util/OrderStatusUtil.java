package com.imthath.foodstreet.order.util;

import com.imthath.foodstreet.order.model.Order;
import com.imthath.foodstreet.order.model.OrderStatus;
import com.imthath.foodstreet.order.model.RestaurantOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderStatusUtil {

    /**
     * Calculate the overall order status based on the statuses of all restaurant orders
     * @param order The order to calculate status for
     * @return The calculated overall status
     */
    public static OrderStatus calculateOverallStatus(Order order) {
        List<RestaurantOrder> restaurantOrders = order.getRestaurantOrders();
        
        if (restaurantOrders == null || restaurantOrders.isEmpty()) {
            return OrderStatus.PAYMENT_PENDING;
        }

        // If any restaurant order is cancelled or payment pending, the whole order is in that state
        if (restaurantOrders.stream().anyMatch(ro -> ro.getStatus() == OrderStatus.CANCELLED)) {
            return OrderStatus.CANCELLED;
        }
        
        if (restaurantOrders.stream().anyMatch(ro -> ro.getStatus() == OrderStatus.PAYMENT_PENDING)) {
            return OrderStatus.PAYMENT_PENDING;
        }

        // If all restaurant orders are completed, the whole order is completed
        if (restaurantOrders.stream().allMatch(ro -> ro.getStatus() == OrderStatus.COMPLETED)) {
            return OrderStatus.COMPLETED;
        }

        // Determine the predominant status
        Map<OrderStatus, Long> statusCounts = restaurantOrders.stream()
                .collect(Collectors.groupingBy(RestaurantOrder::getStatus, Collectors.counting()));

        // Find the status with the most restaurant orders
        return statusCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(OrderStatus.PAYMENT_COMPLETED);
    }
} 