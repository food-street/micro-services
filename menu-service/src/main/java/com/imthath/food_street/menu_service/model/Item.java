package com.imthath.food_street.menu_service.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@Document(collection = "items")
@NoArgsConstructor
public class Item extends BaseEntity {
    private String categoryId;      // Required
    private BigDecimal price;       // Required
}

