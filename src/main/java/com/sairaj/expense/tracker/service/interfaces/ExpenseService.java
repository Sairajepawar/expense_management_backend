package com.sairaj.expense.tracker.service.interfaces;

import com.sairaj.expense.tracker.dto.ExpenseDetails;

import java.util.Map;
import java.util.UUID;

public interface ExpenseService {
    public void createExpense(ExpenseDetails expenseDetails, UUID user_id);
    public void deleteExpense(UUID expenseId, UUID user_id);
    public void editExpense(UUID expenseId, Map<String,Object> updates, UUID user_id);
}
