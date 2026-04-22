package com.sairaj.expense.tracker.service.interfaces;

import com.sairaj.expense.tracker.dto.LoginRequest;
import com.sairaj.expense.tracker.dto.TokenResponse;
import com.sairaj.expense.tracker.dto.UserRequest;

import java.util.UUID;

public interface UserService {

    public void createUser(UserRequest userRequest);
    public TokenResponse loginUser(LoginRequest loginRequest);
    public String getName(UUID userId);
}
