package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import com.PayMyBuddy.MoneyTransfer.service.TransactionService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
@AllArgsConstructor
public class TransactionController {

    private static final Logger logger = LogManager.getLogger("TransactionController");
    private TransactionService transactionService;
    private MyUserDetailsService myUserDetailsService;

    @ModelAttribute("transaction")
    public TransactionDto transactionDto(){ return new TransactionDto(); }

    @GetMapping("/transaction")
    public String showTransactionForm(Model model){
        logger.info("calling method : showTransactionForm");
        model.addAttribute("contactList", myUserDetailsService.getUserContacts());
        return "transaction-form";
    }

    @PostMapping("/transaction")
    public String addTransaction(@ModelAttribute("transaction") TransactionDto transactionDto,
                                 BindingResult result){
        logger.info("calling method : addTransaction");
        transactionService.addTransaction(transactionDto, result);

        if (result.hasErrors()){
            logger.error("cant create transaction : " + result.getAllErrors());
            return "redirect:/transaction?error";
        }

        logger.info("transaction successfully created");
        return "redirect:/transaction?success";
    }

    @GetMapping("/transactions")
    public String showTransactionHistory(Model model){
        logger.info("calling method : showTransactionHistory");
        ArrayList<TransactionDto> transactionasSenderDtos = transactionService.getTransactionAsSenderDtos();
        ArrayList<TransactionDto> transactionasReceiverDtos = transactionService.getTransactionAsReceiverDtos();
        model.addAttribute("transactionsAsSender", transactionasSenderDtos);
        model.addAttribute("transactionsAsReceiver", transactionasReceiverDtos);

        return "transaction-history";
    }

}
