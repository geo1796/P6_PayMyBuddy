package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.RoleRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService /*implements UserDetailsService */{

    UserRepository userRepository;
    RoleRepository roleRepository;

    public Iterable<User> getUsers(){ return userRepository.findAll(); }



}
