package com.sairaj.expense.tracker.service;

import com.sairaj.expense.tracker.dto.LoginRequest;
import com.sairaj.expense.tracker.dto.TokenResponse;
import com.sairaj.expense.tracker.dto.UserRequest;
import com.sairaj.expense.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserService {

    public void createUser(UserRequest userRequest);
    public TokenResponse loginUser(LoginRequest loginRequest);
}
