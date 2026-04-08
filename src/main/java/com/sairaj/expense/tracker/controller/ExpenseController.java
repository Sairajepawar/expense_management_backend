package com.sairaj.expense.tracker.controller;

import com.sairaj.expense.tracker.dto.CustomerUserDetail;
import com.sairaj.expense.tracker.dto.ExpenseCreation;
import com.sairaj.expense.tracker.service.interfaces.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService=expenseService;
    }

    //test api
    @GetMapping("/hello")
    public String getHello(){
        return "Hello World";
    }
    //create
    @PostMapping("/create")
    public ResponseEntity<String> createExpense(@Valid @RequestBody ExpenseCreation expenseCreation, @AuthenticationPrincipal CustomerUserDetail userDetails){
        expenseService.createExpense(expenseCreation,userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Expense Created");
    }
    //delete
    //import
}
