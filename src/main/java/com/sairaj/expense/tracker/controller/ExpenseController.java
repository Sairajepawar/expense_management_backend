package com.sairaj.expense.tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    //test api
    @GetMapping("/hello")
    public String getHello(){
        return "Hello World";
    }
    //create
    //delete
    //import
}
