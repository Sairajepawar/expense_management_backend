package com.sairaj.expense.tracker.model;

import jakarta.persistence.*;
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
    private BigDecimal amount;
    private String category;
    private String description;
    @NotNull
    private LocalDate expenseDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User users;
}
