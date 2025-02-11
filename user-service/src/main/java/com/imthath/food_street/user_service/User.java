package com.imthath.food_street.user_service;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String phoneNumber;
    private Role role;

    public enum Role {
        USER,
        APP_ADMIN,
        R_ADMIN, R_EMP,
        FC_ADMIN, FC_EMP
    }
}