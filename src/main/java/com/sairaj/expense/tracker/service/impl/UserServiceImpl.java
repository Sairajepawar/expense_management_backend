package com.sairaj.expense.tracker.service.impl;

import com.sairaj.expense.tracker.dto.UserRequest;
import com.sairaj.expense.tracker.model.User;
import com.sairaj.expense.tracker.repository.UserRepository;
import com.sairaj.expense.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createUser(UserRequest userRequest){
//        password validation

//        check user existence
//        convert dto into model
//        hashpassword
//        register user

    }
}
