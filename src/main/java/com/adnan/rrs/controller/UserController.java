package com.adnan.rrs.controller;

import com.adnan.rrs.dto.RegisterRequest;
import com.adnan.rrs.entity.User;
import com.adnan.rrs.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }
}
