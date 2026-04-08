package com.sairaj.expense.tracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ExpenseDetails {
    @NotNull
    @Min(value=0)
    private double amount;
    @NotBlank
    private String category;
    @NotBlank
    @Size(
            min = 5,
            max = 255
    )
    private String description;
}
