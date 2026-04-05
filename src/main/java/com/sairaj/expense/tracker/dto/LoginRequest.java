package com.sairaj.expense.tracker.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$",
            message = "Password must be contain atleast one character with uppercase, lowercase, number, and symbol"
    )
    @Size(
            min = 8,
            max = 64,
            message = "Password must be within length of 8 to 64"
    )
    private String password;
}
