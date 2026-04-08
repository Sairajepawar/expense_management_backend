package com.sairaj.expense.tracker.service.interfaces;

import com.sairaj.expense.tracker.dto.ExpenseCreation;

import java.util.UUID;

public interface ExpenseService {
    public void createExpense(ExpenseCreation expenseCreation, UUID id);
}
