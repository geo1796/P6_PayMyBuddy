package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountDto;
import com.PayMyBuddy.MoneyTransfer.model.BankAccount;
import com.PayMyBuddy.MoneyTransfer.service.BankAccountService;
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

import java.util.HashSet;
import java.util.List;


@Controller
@AllArgsConstructor
public class BankAccountController {

    private static final Logger logger = LogManager.getLogger("BankAccountController");
    private BankAccountService bankAccountService;
    private List<ObjectError> errors;

    @ModelAttribute("bankAccount")
    public BankAccount bankAccount() {
        return new BankAccount();
    }

    @GetMapping("/bankAccounts")
    public String showBankAccounts(Model model){
        logger.info("calling method : showBankAccounts");

        HashSet<BankAccountDto> bankAccounts = (HashSet<BankAccountDto>) bankAccountService.findAllDtos();
        model.addAttribute("bankAccounts", bankAccounts);

        return "bank-accounts";
    }

    @GetMapping("/addBankAccount")
    public String showBankAccountForm(Model model){
        model.addAttribute("errors", errors);
        logger.info("calling method : showBankAccountForm");
        return "bank-account-form";
    }

    @PostMapping("/addBankAccount")
    public String addBankAccount(@ModelAttribute("bankAccount") BankAccount bankAccount, BindingResult result){
        logger.info("calling method : showBankAccountForm");

        bankAccountService.addBankAccount(bankAccount, result);

        if (result.hasErrors()){
            logger.error("can't add bank account");
            errors = result.getAllErrors();
            return "redirect:/addBankAccount?error";
        }

        logger.info("bank account added");
        return "redirect:/addBankAccount?success";
    }


}
