package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardTransactionDto;
import com.PayMyBuddy.MoneyTransfer.service.CreditCardService;
import com.PayMyBuddy.MoneyTransfer.service.CreditCardTransactionService;
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

import java.util.List;

@Controller
@AllArgsConstructor
public class CreditCardTransactionController {

    private static final Logger logger = LogManager.getLogger("CreditCardTransactionController");
    private CreditCardTransactionService creditCardTransactionService;
    private CreditCardService creditCardService;
    private List<ObjectError> errors;

    @ModelAttribute("creditCardTransaction")
    public CreditCardTransactionDto creditCardTransactionDto(){ return new CreditCardTransactionDto(); }

    @GetMapping("/creditCardTransactions")
    public String showCreditCardTransactions(Model model){
        logger.info("calling method : showCreditCardTransactions");

        model.addAttribute("creditCardTransactions", creditCardTransactionService.getCreditCardTransactionDtos());

        return "credit-card-transaction-history";
    }

    @GetMapping("/creditCardTransaction")
    public String showCreditCardTransactionForm(Model model){
        logger.info("calling method : showCreditCardTransactionForm");

        model.addAttribute("creditCards", creditCardService.getAllUsersCreditCards());
        model.addAttribute("errors", errors);

        return "credit-card-transaction-form";
    }

    @PostMapping("/creditCardTransaction")
    public String fromCreditCardToBalance(@ModelAttribute("creditCardTransaction") CreditCardTransactionDto creditCardTransactionDto, BindingResult result){

        logger.info("calling method : fromCreditCardToBalance");
        creditCardTransactionService.addCreditCardTransaction(creditCardTransactionDto, result);

        if (result.hasErrors()){
            logger.error("can't proceed transaction");
            errors = result.getAllErrors();
            return "redirect:/creditCardTransaction?error";
        }

        logger.info("credit card transaction success");
        return "redirect:/creditCardTransaction?success";
    }
}
