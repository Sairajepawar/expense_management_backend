package com.sairaj.expense.tracker.service;

import com.sairaj.expense.tracker.dto.CustomerUserDetail;
import com.sairaj.expense.tracker.model.User;
import com.sairaj.expense.tracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomerUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(String.format("User with %s email doesn't exists",email)));
        return CustomerUserDetail.builder().id(user.getId()).email(user.getEmail()).password(user.getPassword()).build();
    }

    public UserDetails loadUserById(UUID id) throws UsernameNotFoundException{
        User user = userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException(String.format("User with %s id doesn't exists",id)));
        return CustomerUserDetail.builder().id(id).email(user.getEmail()).password(user.getPassword()).build();
    }
}
