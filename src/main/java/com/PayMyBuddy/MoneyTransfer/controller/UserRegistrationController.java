package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.UserRegistrationDto;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class UserRegistrationController {

    private MyUserDetailsService myUserDetailsService;
/*
    @GetMapping("/registration")
    public String showRegistrationForm(){ return "registration"; }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto(){
        return new UserRegistrationDto();
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user")UserRegistrationDto dto){
        myUserDetailsService.saveUser(dto);
        return "redirect:/registration?success";
    }*/

}
