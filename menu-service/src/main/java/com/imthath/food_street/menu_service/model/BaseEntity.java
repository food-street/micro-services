package com.imthath.food_street.menu_service.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@SuperBuilder
@Data
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    private String id;

    private long restaurantId;      // Required
    private String name;            // Required
    private String description;     // Optional
    private String imageUrl;        // Optional
    private boolean isAvailable;    // Required
    private int displayOrder;       // Required

    @CreatedDate
    @Field("created_at")
    private Date createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Date updatedAt;
}

