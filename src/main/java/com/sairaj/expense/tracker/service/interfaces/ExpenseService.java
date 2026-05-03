package com.sairaj.expense.tracker.service.interfaces;

import com.sairaj.expense.tracker.dto.ExpenseDetails;
import com.sairaj.expense.tracker.model.Expense;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExpenseService {
    public void createExpense(ExpenseDetails expenseDetails, UUID userId);
    public void deleteExpense(UUID expenseId, UUID userId);
    public void editExpense(UUID expenseId, Map<String,Object> updates, UUID userId);
    public List<ExpenseDetails> listExpenses(LocalDate from, LocalDate to, UUID userId);
    public String exportExpenses(LocalDate from,LocalDate to, String Filetype, UUID userId);
}
