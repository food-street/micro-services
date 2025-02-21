package com.imthath.food_street.menu_service.repo;

import com.imthath.food_street.menu_service.model.Category;
import com.imthath.food_street.menu_service.model.Item;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import jakarta.annotation.PostConstruct;

@Configuration
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    public MongoIndexConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void createIndexes() {
        // Index for restaurant-based search
        mongoTemplate.indexOps(Item.class).ensureIndex(
                new Index().on("item_restaurantId", org.springframework.data.domain.Sort.Direction.ASC)
                        .on("name", org.springframework.data.domain.Sort.Direction.ASC)
                        .on("description", org.springframework.data.domain.Sort.Direction.ASC)
        );

        // Replace the compound index creation with:
        mongoTemplate.indexOps(Item.class).ensureIndex(
            new Index()
                    .named("restaurant_search_idx")
                    .on("restaurantId", org.springframework.data.domain.Sort.Direction.ASC)
                    .on("name", org.springframework.data.domain.Sort.Direction.ASC)
                    .on("description", org.springframework.data.domain.Sort.Direction.ASC)
        );

        // Index for categories
        mongoTemplate.indexOps(Category.class).ensureIndex(
                new Index().on("category_restaurantId", org.springframework.data.domain.Sort.Direction.ASC)
                        .on("name", org.springframework.data.domain.Sort.Direction.ASC)
        );
    }
}

