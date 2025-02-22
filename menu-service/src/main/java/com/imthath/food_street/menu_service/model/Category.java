package com.imthath.food_street.menu_service.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@Document(collection = "categories")
@NoArgsConstructor
public class Category extends BaseEntity {
}

