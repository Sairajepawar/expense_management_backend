package com.sairaj.expense.tracker.controller;

import com.sairaj.expense.tracker.dto.CustomerUserDetail;
import com.sairaj.expense.tracker.dto.ExpenseDetails;
import com.sairaj.expense.tracker.service.interfaces.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public ResponseEntity<String> createExpense(@Valid @RequestBody ExpenseDetails expenseDetails, @AuthenticationPrincipal CustomerUserDetail userDetails){
        expenseService.createExpense(expenseDetails,userDetails.getId());
        return ResponseEntity.ok("Expense Created");
    }
    //delete
    @PutMapping("/delete")
    public ResponseEntity<String> deleteExpense(@RequestParam UUID expenseId, @AuthenticationPrincipal CustomerUserDetail userDetail){
        expenseService.deleteExpense(expenseId,userDetail.getId());
        return ResponseEntity.ok("Expense Delete Successfully");
    }
    //edit
    @PatchMapping ("/edit")
    public ResponseEntity<String> editExpense(
            @RequestParam UUID expenseId,
            @RequestBody Map<String,Object> updates,
            @AuthenticationPrincipal CustomerUserDetail userDetail){
        expenseService.editExpense(expenseId,updates,userDetail.getId());
        return ResponseEntity.ok("Expense Edited");
    }
    //list
    @GetMapping("/list")
    public ResponseEntity<List<ExpenseDetails>> listExpenses(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @AuthenticationPrincipal CustomerUserDetail userDetail){
        List<ExpenseDetails> expenseDetails = expenseService.listExpenses(from,to,userDetail.getId());
        return ResponseEntity.ok(expenseDetails);
    }
}
