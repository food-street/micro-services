package com.imthath.food_street.menu_service.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "categories")
public class Category extends BaseEntity {
}

