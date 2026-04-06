package com.sairaj.expense.tracker.service.impl;

import com.sairaj.expense.tracker.dto.LoginRequest;
import com.sairaj.expense.tracker.dto.TokenResponse;
import com.sairaj.expense.tracker.dto.UserRequest;
import com.sairaj.expense.tracker.exceptions.EmailAlreadyExistException;
import com.sairaj.expense.tracker.exceptions.InvalidCrendentialException;
import com.sairaj.expense.tracker.model.User;
import com.sairaj.expense.tracker.repository.UserRepository;
import com.sairaj.expense.tracker.service.JwtService;
import com.sairaj.expense.tracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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

    @Override
    public TokenResponse loginUser(LoginRequest loginRequest){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        if(auth.isAuthenticated()){
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new UsernameNotFoundException("Invalid Input"));
            TokenResponse tokenResponse = TokenResponse.builder().token(jwtService.generateToken(user.getId())).build();
            return tokenResponse;
        }
        else{
            throw new InvalidCrendentialException("Invalid Credentials");
        }
    }
}

