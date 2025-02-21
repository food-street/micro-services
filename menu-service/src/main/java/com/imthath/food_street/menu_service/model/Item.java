package com.imthath.food_street.menu_service.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "items")
public class Item extends BaseEntity {
    private String categoryId;      // Required
    private BigDecimal price;       // Required
}

