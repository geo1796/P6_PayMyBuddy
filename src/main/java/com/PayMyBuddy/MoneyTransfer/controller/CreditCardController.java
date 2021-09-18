package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardDto;
import com.PayMyBuddy.MoneyTransfer.service.CreditCardService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class CreditCardController {

    private static final Logger logger = LogManager.getLogger("CreditCardController");
    private CreditCardService creditCardService;
    private List<ObjectError> errors;

    @ModelAttribute("creditCard")
    public CreditCardDto creditCardDto(){ return new CreditCardDto(); }

    @GetMapping("creditCards")
    public String showCreditCards(Model model){
        logger.info("calling method : showCreditCards");
        model.addAttribute("creditCards", creditCardService.findCreditCardDtos());
        return "credit-cards";
    }

    @GetMapping("/addCreditCard")
    public String showCreditCardForm(Model model){
        logger.info("calling method : showCreditCardForm");
        model.addAttribute("errors", errors);
        return "credit-card-form";
    }

    @PostMapping("addCreditCard")
    public String addCreditCard(@ModelAttribute("creditCard") @Valid CreditCardDto creditCardDto, BindingResult result){
        logger.info("calling method : addCreditCard");
        creditCardService.addCreditCard(creditCardDto, result);

        if(result.hasErrors()){
            errors = result.getAllErrors();
            logger.error("can't add credit card");
            return "redirect:/addCreditCard?error";
        }

        logger.info("credit card successfully added to the pay my buddy account");
        return "redirect:/addCreditCard?success";
    }

}
