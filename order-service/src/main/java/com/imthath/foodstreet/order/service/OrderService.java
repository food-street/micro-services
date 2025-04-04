package com.imthath.foodstreet.order.service;

import com.imthath.foodstreet.order.client.CartClient;
import com.imthath.foodstreet.order.client.RestaurantClient;
import com.imthath.foodstreet.order.model.*;
import com.imthath.foodstreet.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final RestaurantClient restaurantClient;

    @Transactional
    public Order createOrder(String userId) {
        Cart cart = cartClient.getCart(userId);
        if (cart == null || cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // Validate all items are from the same food court
        String foodCourtId = restaurantClient.getRestaurantFoodCourt(cart.items()[0].restaurantId());
        for (CartItem item : cart.items()) {
            String itemFoodCourtId = restaurantClient.getRestaurantFoodCourt(item.restaurantId());
            if (!foodCourtId.equals(itemFoodCourtId)) {
                throw new IllegalStateException("All items must be from the same food court");
            }
        }

        // Validate all items are available
        for (CartItem item : cart.items()) {
            boolean isAvailable = restaurantClient.checkMenuItemAvailability(
                item.restaurantId(),
                item.menuItemId()
            );
            if (!isAvailable) {
                throw new IllegalStateException("Item " + item.name() + " is not available");
            }
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setFoodCourtId(foodCourtId);
        order.setFoodCourtName("Food Court Name"); // TODO: Get from food court service
        order.setTotal(cart.total());
        order.setStatus(OrderStatus.PAYMENT_PENDING);

        List<OrderItem> orderItems = Arrays.stream(cart.items())
            .map(cartItem -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setMenuItemId(cartItem.menuItemId());
                orderItem.setRestaurantId(cartItem.restaurantId());
                orderItem.setRestaurantName("Restaurant Name"); // TODO: Get from restaurant service
                orderItem.setName(cartItem.name());
                orderItem.setPrice(cartItem.price());
                orderItem.setQuantity(cartItem.quantity());
                orderItem.setNotes(cartItem.notes());
                return orderItem;
            })
            .collect(Collectors.toList());

        order.setItems(orderItems);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByFoodCourt(String foodCourtId) {
        return orderRepository.findByFoodCourtId(foodCourtId);
    }

    public List<Order> getOrdersByRestaurant(String restaurantId) {
        return orderRepository.findByItemsRestaurantId(restaurantId);
    }
}