package com.imthath.foodstreet.order.service;

import com.imthath.foodstreet.order.client.CartClient;
import com.imthath.foodstreet.order.client.RestaurantClient;
import com.imthath.foodstreet.order.model.*;
import com.imthath.foodstreet.order.repository.OrderRepository;
import com.imthath.foodstreet.order.util.OrderStatusUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final RestaurantClient restaurantClient;
    private final OrderEventService orderEventService;

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

        final Order order = new Order();
        order.setUserId(userId);
        order.setFoodCourtId(foodCourtId);
        order.setFoodCourtName("Food Court Name"); // TODO: Get from food court service
        order.setTotal(cart.total());
        order.setOverallStatus(OrderStatus.PAYMENT_PENDING);

        // Group cart items by restaurant
        Map<String, List<CartItem>> itemsByRestaurant = Arrays.stream(cart.items())
            .collect(Collectors.groupingBy(CartItem::restaurantId));

        // Create restaurant orders for each restaurant
        for (Map.Entry<String, List<CartItem>> entry : itemsByRestaurant.entrySet()) {
            String restaurantId = entry.getKey();
            List<CartItem> restaurantItems = entry.getValue();
            
            RestaurantOrder restaurantOrder = new RestaurantOrder();
            restaurantOrder.setOrder(order);
            restaurantOrder.setRestaurantId(restaurantId);
            restaurantOrder.setRestaurantName("Restaurant Name"); // TODO: Get from restaurant service
            restaurantOrder.setStatus(OrderStatus.PAYMENT_PENDING);
            
            List<OrderItem> orderItems = restaurantItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setRestaurantOrder(restaurantOrder);
                    orderItem.setMenuItemId(cartItem.menuItemId());
                    orderItem.setName(cartItem.name());
                    orderItem.setPrice(cartItem.price());
                    orderItem.setQuantity(cartItem.quantity());
                    orderItem.setNotes(cartItem.notes());
                    return orderItem;
                })
                .collect(Collectors.toList());
            
            restaurantOrder.setItems(orderItems);
            order.getRestaurantOrders().add(restaurantOrder);
        }

        // no need for sending order updates from here.
        // Client doesn't even know the order id to subscribe updates at this point.
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(String orderId, String restaurantId, OrderStatus status) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        // Find the restaurant order and update its status
        order.getRestaurantOrders().stream()
            .filter(ro -> ro.getRestaurantId().equals(restaurantId))
            .findFirst()
            .ifPresent(restaurantOrder -> restaurantOrder.setStatus(status));
        
        // Recalculate the overall order status
        order.setOverallStatus(OrderStatusUtil.calculateOverallStatus(order));
        
        final Order updatedOrder = orderRepository.save(order);
        // Send order status update
        orderEventService.sendOrderUpdate(orderId, order);
        return updatedOrder;
    }

    @Transactional
    public Order updateAllOrderStatuses(String orderId, OrderStatus status) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        // Update status for all restaurant orders
        order.getRestaurantOrders().forEach(ro -> ro.setStatus(status));
        
        // Set the overall status
        order.setOverallStatus(status);
        
        final Order updatedOrder = orderRepository.save(order);
        // Send order status update
        orderEventService.sendOrderUpdate(orderId, order);
        return updatedOrder;
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByFoodCourt(String foodCourtId) {
        return orderRepository.findByFoodCourtId(foodCourtId);
    }

    public List<Order> getOrdersByRestaurant(String restaurantId) {
        return orderRepository.findByRestaurantOrdersRestaurantId(restaurantId);
    }
}