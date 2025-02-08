package com.imthath.food_street.user_service;

import com.imthath.food_street.user_service.error.CommonError;
import com.imthath.food_street.user_service.error.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updateName(String name, String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new GenericException(701, CommonError.USER_NOT_FOUND);
        }
        user.get().setName(name);
        userRepository.save(user.get());
    }
}
