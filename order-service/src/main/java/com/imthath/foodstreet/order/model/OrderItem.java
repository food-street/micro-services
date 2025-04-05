package com.imthath.foodstreet.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotBlank(message = "Menu item ID is required")
    @Column(nullable = false)
    private String menuItemId;

    @NotBlank(message = "Restaurant ID is required")
    @Column(nullable = false)
    private String restaurantId;

    @NotBlank(message = "Restaurant name is required")
    @Column(nullable = false)
    private String restaurantName;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    @Column(nullable = false)
    private double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private int quantity;

    private String notes;
} 