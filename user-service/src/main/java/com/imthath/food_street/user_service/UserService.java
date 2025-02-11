package com.imthath.food_street.user_service;

import com.imthath.food_street.user_service.error.CommonError;
import com.imthath.food_street.user_service.error.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updateName(String name, String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new GenericException(CommonError.USER_NOT_FOUND);
        }
        user.get().setName(name);
        userRepository.save(user.get());
    }

    public User create(String phone, String name, User.Role role) {
        if (userRepository.findByPhoneNumber(phone).isPresent()) {
            throw new GenericException(CommonError.USER_EXISTS);
        }
        var user = new User();
        user.setName(name);
        user.setPhoneNumber(phone);
        user.setRole(role);
        userRepository.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getByPhone(String phone) {
        return userRepository.findByPhoneNumber(phone);
    }
}
