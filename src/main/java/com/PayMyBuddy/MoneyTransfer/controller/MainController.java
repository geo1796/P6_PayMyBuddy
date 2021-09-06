package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Controller
@AllArgsConstructor
public class MainController {

    private static final Logger logger = LogManager.getLogger("MainController");
    private MyUserDetailsService myUserDetailsService;

    @GetMapping("/")
    public String root(Model model) {
        logger.info("calling method : root");
        model.addAttribute("user", myUserDetailsService.findUser());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        logger.info("calling method : login");
        return "login";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        logger.info("calling method : admin");
        return "admin";
    }
}
