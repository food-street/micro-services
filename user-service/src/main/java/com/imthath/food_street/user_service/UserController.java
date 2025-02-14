package com.imthath.food_street.user_service;

import com.imthath.food_street.user_service.security.ValidPhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody UserRequest user) {
        return userService.create(user.phone, user.name, user.role);
    }

    @GetMapping("/phone/{phone}")
    public Optional<User> getByPhone(@PathVariable @ValidPhoneNumber String phone) {
        return userService.getByPhone(phone);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable String id, @RequestParam String name) {
        userService.updateName(name, id);
    }

    public record UserRequest(String phone, String name, User.Role role) {}
}
