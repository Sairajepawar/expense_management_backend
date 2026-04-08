package com.sairaj.expense.tracker.service.impl;

import com.sairaj.expense.tracker.dto.ExpenseDetails;
import com.sairaj.expense.tracker.dto.ExpenseId;
import com.sairaj.expense.tracker.exceptions.OwnershipViolationException;
import com.sairaj.expense.tracker.model.Expense;
import com.sairaj.expense.tracker.repository.ExpenseRepository;
import com.sairaj.expense.tracker.service.interfaces.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public void deleteExpense(ExpenseId expenseId, UUID user_id){
        Expense expense = expenseRepository.findById(UUID.fromString(expenseId.getExpense_id())).orElseThrow(
                ()->new EntityNotFoundException("Expense entry exists")
        );
//        check if expense entry belongs to user
        log.info(expense.getUser_id().toString());
        log.info(user_id.toString());
        if(!(expense.getUser_id().equals(user_id))){
            throw new OwnershipViolationException("Access denied: this expense is not associated with the current user.");
        }
        expenseRepository.delete(expense);
    }
}
