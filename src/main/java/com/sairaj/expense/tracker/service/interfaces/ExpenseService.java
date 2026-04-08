package com.sairaj.expense.tracker.service.interfaces;

import com.sairaj.expense.tracker.dto.ExpenseDetails;
import com.sairaj.expense.tracker.dto.ExpenseId;

import java.util.UUID;

public interface ExpenseService {
    public void createExpense(ExpenseDetails expenseDetails, UUID user_id);
    public void deleteExpense(ExpenseId expenseId, UUID user_id);
}
