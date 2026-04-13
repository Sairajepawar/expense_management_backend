package com.sairaj.expense.tracker.service.impl;

import com.sairaj.expense.tracker.dto.ExpenseDetails;
import com.sairaj.expense.tracker.exceptions.OwnershipViolationException;
import com.sairaj.expense.tracker.model.Expense;
import com.sairaj.expense.tracker.repository.ExpenseRepository;
import com.sairaj.expense.tracker.service.interfaces.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
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
    public void createExpense(ExpenseDetails expenseDetails, UUID user_id){
        Expense expense = modelMapper.map(expenseDetails,Expense.class);
        expense.setExpenseDate(LocalDate.now());
        expense.setUser_id(user_id);
        expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(UUID expenseId, UUID user_id){
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(
                ()->new EntityNotFoundException("Expense entry doesn't exists")
        );
        if(!(expense.getUser_id().equals(user_id))){
            throw new OwnershipViolationException("Access denied: this expense is not associated with the current user.");
        }
        expenseRepository.delete(expense);
    }

    @Override
    public void editExpense(UUID expenseId, Map<String,Object> updates, UUID user_id){
        ObjectMapper mapper = new ObjectMapper();
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(
                ()->new EntityNotFoundException("Expense entry doesn't exists")
        );
        if(!(expense.getUser_id().equals(user_id))){
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
}
