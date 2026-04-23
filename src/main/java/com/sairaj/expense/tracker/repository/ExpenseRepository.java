package com.sairaj.expense.tracker.repository;

import com.sairaj.expense.tracker.dto.ExpenseDetails;
import com.sairaj.expense.tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByExpenseDateBetweenAndUserid(LocalDate from, LocalDate to, UUID userId);

    List<Expense> findByUserid(UUID userId);
}
