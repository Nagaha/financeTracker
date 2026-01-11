package com.webapp.finance.controller;

import com.webapp.finance.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public User getLoggedInUser(@AuthenticationPrincipal User user) {
        return user;
    }
}
