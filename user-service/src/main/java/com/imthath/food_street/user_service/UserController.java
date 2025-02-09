package com.imthath.food_street.user_service;

import com.imthath.food_street.user_service.error.CommonError;
import com.imthath.food_street.user_service.error.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable String id, @RequestParam String name) {
        userService.updateName(name, id);
    }
}
