package com.sairaj.expense.tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @Email
    @NotNull
    private String email;

    @NotNull
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
