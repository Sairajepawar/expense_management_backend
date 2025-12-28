package com.sairaj.expense.tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/health")
    public ResponseEntity<String> demo(){
        return ResponseEntity.ok().body("Backend Service is running");
    }

//    @PostMapping("/create")
//    public ResponseEntity
}
