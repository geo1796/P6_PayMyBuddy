package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.UserRegistrationDto;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/registration")
@AllArgsConstructor
public class UserRegistrationController {

    private MyUserDetailsService myUserDetailsService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        Optional<User> optionalUser = myUserDetailsService.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            return "registration";
        }

        myUserDetailsService.save(userDto);
        return "redirect:/registration?success";
    }
}

