package com.PayMyBuddy.MoneyTransfer.integration.controller;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountTransactionDto;
import com.PayMyBuddy.MoneyTransfer.service.BankAccountService;
import com.PayMyBuddy.MoneyTransfer.service.BankAccountTransactionService;
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
public class BankAccountTransactionController {

    private BankAccountTransactionService bankAccountTransactionService;
    private static final Logger logger = LogManager.getLogger("BankAccountTransactionController");
    private BankAccountService bankAccountService;
    private List<ObjectError> errors;

    @ModelAttribute("bankAccountTransaction")
    BankAccountTransactionDto bankAccountTransactionDto(){ return new BankAccountTransactionDto(); }

    @GetMapping("/bankAccountTransactions")
    public String showBankAccountTransactions(Model model) {
        logger.info("calling method : showBankAccountTransactions");

        model.addAttribute("bankAccountTransactions", bankAccountTransactionService.findBankAccountTransactionDtos());

        return "bank-account-transactions-history";
    }

    @GetMapping("/bankAccountTransaction")
    public String showBankAccountTransactionForm(Model model){
        logger.info("calling method : showBankAccountTransactionForm");

        model.addAttribute("bankAccounts", bankAccountService.findAllDtos());
        model.addAttribute("errors", errors);

        return "bank-account-transaction-form";
    }

    @PostMapping("/bankAccountTransaction")
    public String addBankAccountTransaction(@ModelAttribute("bankAccountTransaction")BankAccountTransactionDto bankAccountTransaction, BindingResult result, Model model){
        logger.info("calling method : addBankAccountTransaction");

        bankAccountTransactionService.addBankAccountTransaction(bankAccountTransaction, result);

        if (result.hasErrors()){
            logger.error("can't proceed transaction");
            errors = result.getAllErrors();
            return "redirect:/bankAccountTransaction?error";
        }

        logger.info("bank account transaction success");
        return "redirect:/bankAccountTransaction?success";
    }


}
