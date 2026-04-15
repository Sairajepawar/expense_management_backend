package com.sairaj.expense.tracker.service.impl;

import com.sairaj.expense.tracker.dto.ExpenseDetails;
import com.sairaj.expense.tracker.exceptions.OwnershipViolationException;
import com.sairaj.expense.tracker.model.Expense;
import com.sairaj.expense.tracker.repository.ExpenseRepository;
import com.sairaj.expense.tracker.service.interfaces.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private ExpenseRepository expenseRepository;
    private ModelMapper modelMapper;

    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository,
            ModelMapper modelMappper){
        this.expenseRepository = expenseRepository;
        this.modelMapper = modelMappper;
    }

    @Override
    public void createExpense(ExpenseDetails expenseDetails, UUID userId){
        Expense expense = modelMapper.map(expenseDetails,Expense.class);
        expense.setExpenseDate(LocalDate.now());
        expense.setUserid(userId);
        expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(UUID expenseId, UUID userId){
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(
                ()->new EntityNotFoundException("Expense entry doesn't exists")
        );
        if(!(expense.getUserid().equals(userId))){
            throw new OwnershipViolationException("Access denied: this expense is not associated with the current user.");
        }
        expenseRepository.delete(expense);
    }

    @Override
    public void editExpense(UUID expenseId, Map<String,Object> updates, UUID userId){
        ObjectMapper mapper = new ObjectMapper();
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(
                ()->new EntityNotFoundException("Expense entry doesn't exists")
        );
        if(!(expense.getUserid().equals(userId))){
            throw new OwnershipViolationException("Access denied: this expense is not associated with the current user.");
        }
        for(Map.Entry<String,Object> update:updates.entrySet()){
            switch (update.getKey()){
                case "amount" -> expense.setAmount(mapper.convertValue(update.getValue(), Double.class));
                case "category" -> expense.setCategory(mapper.convertValue(update.getValue(),String.class));
                case "description" -> expense.setDescription(mapper.convertValue(update.getValue(),String.class));
                default -> throw new IllegalArgumentException("Not allowed: " + update.getKey());
            }
        }
        expenseRepository.save(expense);
    }

    @Override
    public List<ExpenseDetails> listExpenses(
            LocalDate from,
            LocalDate to,
            UUID userId){
        List<Expense> expenses;
        if(from==null && to==null){
            expenses = expenseRepository.findByUserid(userId);
        }
        else if(from==null || to==null){
            throw new IllegalArgumentException("Provide both 'from' and 'to', or neither");
        }
        else if(from.isAfter(to)){
            throw new IllegalArgumentException("'from' must not be after 'to'");
        }
        else{
            expenses = expenseRepository.findByExpenseDateBetweenAndUserid(from,to,userId);
        }
        return expenses.stream()
                .map(expense -> modelMapper.map(expense,ExpenseDetails.class))
                .toList();
    }
}
