package com.sairaj.expense.tracker.service.impl;

import com.sairaj.expense.tracker.dto.UserRequest;
import com.sairaj.expense.tracker.exceptions.EmailAlreadyExistException;
import com.sairaj.expense.tracker.model.User;
import com.sairaj.expense.tracker.repository.UserRepository;
import com.sairaj.expense.tracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(UserRequest userRequest){
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new EmailAlreadyExistException(String.format("User with email %s already exists",userRequest.getEmail()));
        }
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User user = modelMapper.map(userRequest,User.class);
        userRepository.save(user);
    }


}

