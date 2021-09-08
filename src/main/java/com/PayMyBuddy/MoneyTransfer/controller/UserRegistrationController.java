package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.UserRegistrationDto;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/registration")
@AllArgsConstructor
public class UserRegistrationController {

    private static final Logger logger = LogManager.getLogger("UserRegistrationController");
    private MyUserDetailsService myUserDetailsService;
    private List<ObjectError> errors;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        logger.info("calling method : showRegistrationForm");
        model.addAttribute("errors", errors);
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {
        logger.info("calling method : registerUserAccount");

        Optional<User> optionalUser = myUserDetailsService.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            result.reject("user", "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            errors = result.getAllErrors();
            logger.error("can't register user");
            return "redirect:/registration?error";
        }

        myUserDetailsService.save(userDto);
        logger.info("user registration success");
        return "redirect:/registration?success";
    }
}

