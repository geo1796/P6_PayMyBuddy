package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardDto;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.service.CreditCardService;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class CreditCardController {

    private static final Logger logger = LogManager.getLogger("CreditCardController");
    private CreditCardService creditCardService;
    private MyUserDetailsService myUserDetailsService;

    @ModelAttribute("creditCard")
    public CreditCardDto creditCardDto(){ return new CreditCardDto(); }

    @GetMapping("creditCards")
    public String showCreditCards(Model model){
        User user = myUserDetailsService.findUser();
        model.addAttribute("creditCards", user.getCreditCards());
        return "credit-cards";
    }

    @GetMapping("/addCreditCard")
    public String showCreditCardForm(){
        return "credit-card-form";
    }

    @PostMapping("addCreditCard")
    public String addCreditCard(@ModelAttribute("creditCard") @Valid CreditCardDto creditCardDto, BindingResult result){

        creditCardService.addCreditCard(creditCardDto, result);

        if(result.hasErrors()){
            result.getAllErrors().forEach(
                    error -> logger.error(error.toString())
            );
            return "redirect:/addCreditCard?error";
        }

        logger.info("credit card successfully added to the pay my buddy account");
        return "redirect:/addCreditCard?success";
    }

}
