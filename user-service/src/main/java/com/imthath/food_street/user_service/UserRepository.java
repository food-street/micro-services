package com.imthath.food_street.user_service;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByPhoneNumber(String phoneNumber);
}