package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.UserDto;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    private MyUserDetailsService myUserDetailsService;

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") final int id) {
        Optional<UserDto> optionalUser = myUserDetailsService.getUser(id);

        if(optionalUser.isPresent()){
            UserDto userFound = optionalUser.get();
            return new ResponseEntity<>(userFound, HttpStatus.OK);
        }

        return new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND);
    }

}
