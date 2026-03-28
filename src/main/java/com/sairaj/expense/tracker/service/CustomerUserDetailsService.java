package com.sairaj.expense.tracker.service;

import com.sairaj.expense.tracker.dto.CustomerUserDetail;
import com.sairaj.expense.tracker.model.User;
import com.sairaj.expense.tracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomerUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User doesn't exists"));
        return CustomerUserDetail.builder().email(user.getEmail()).password(user.getPassword()).build();
    }
}
