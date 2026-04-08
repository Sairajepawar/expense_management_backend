package com.sairaj.expense.tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private double amount;
    @NotBlank
    private String category;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate expenseDate;
    @NotNull
    private UUID user_id;
}
