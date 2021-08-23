package com.PayMyBuddy.MoneyTransfer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.RolesAllowed;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/index")
    public String index (){ return "index"; }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/home")
    public String admin(){ return "adminHome"; }
}
