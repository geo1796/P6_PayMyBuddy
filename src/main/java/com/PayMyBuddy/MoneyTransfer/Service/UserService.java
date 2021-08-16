package com.PayMyBuddy.MoneyTransfer.Service;

import com.PayMyBuddy.MoneyTransfer.Model.User;
import com.PayMyBuddy.MoneyTransfer.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public Iterable<User> getUsers(){ return userRepository.findAll(); }

}
