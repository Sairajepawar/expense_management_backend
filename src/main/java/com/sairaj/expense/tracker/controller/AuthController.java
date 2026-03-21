package com.sairaj.expense.tracker.controller;

import com.sairaj.expense.tracker.dto.UserRequest;
import com.sairaj.expense.tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createuser(@Valid @RequestBody UserRequest userRequest){
        userService.createUser(userRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User Created Successfully");
    }
}
