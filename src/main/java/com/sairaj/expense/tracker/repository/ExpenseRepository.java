package com.sairaj.expense.tracker.repository;

import com.sairaj.expense.tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
}
