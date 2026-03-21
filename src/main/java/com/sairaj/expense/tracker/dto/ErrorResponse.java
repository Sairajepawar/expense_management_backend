package com.sairaj.expense.tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ErrorResponse {
    String message;
    LocalDate date;
}
