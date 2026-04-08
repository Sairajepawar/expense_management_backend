package com.sairaj.expense.tracker.service.impl;

import com.sairaj.expense.tracker.dto.ExpenseCreation;
import com.sairaj.expense.tracker.model.Expense;
import com.sairaj.expense.tracker.repository.ExpenseRepository;
import com.sairaj.expense.tracker.service.interfaces.ExpenseService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

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
    public void createExpense(ExpenseCreation expenseCreation, UUID id){
        Expense expense = modelMapper.map(expenseCreation,Expense.class);
        expense.setExpenseDate(LocalDate.now());
        expense.setUser_id(id);
        expenseRepository.save(expense);
    }
}
