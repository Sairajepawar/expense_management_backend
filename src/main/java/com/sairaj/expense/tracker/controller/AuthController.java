package com.sairaj.expense.tracker.controller;

import com.sairaj.expense.tracker.dto.UserRequest;
import com.sairaj.expense.tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createuser(@Valid @RequestBody  UserRequest userRequest){
        userService.
        return ResponseEntity.ok("Usercreate Successfully");
    }
}
