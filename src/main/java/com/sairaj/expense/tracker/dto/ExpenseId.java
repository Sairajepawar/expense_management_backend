package com.sairaj.expense.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExpenseId {
    @NotBlank
    private String expense_id;
}
