package com.sairaj.expense.tracker.controller;

import com.sairaj.expense.tracker.dto.LoginRequest;
import com.sairaj.expense.tracker.dto.TokenResponse;
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        TokenResponse tokenResponse = userService.loginUser(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tokenResponse);
    }
}
