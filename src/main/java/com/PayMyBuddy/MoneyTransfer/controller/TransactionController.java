package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import com.PayMyBuddy.MoneyTransfer.service.TransactionService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
public class TransactionController {

    private static final Logger logger = LogManager.getLogger("TransactionController");
    private TransactionService transactionService;
    private MyUserDetailsService myUserDetailsService;
    private List<ObjectError> errors;

    @ModelAttribute("transaction")
    public TransactionDto transactionDto(){ return new TransactionDto(); }

    @GetMapping("/transaction")
    public String showTransactionForm(Model model, @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size){
        logger.info("calling method : showTransactionForm");

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<TransactionDto> transactionDtoPage = transactionService.findPaginated(PageRequest.of(currentPage - 1, pageSize), transactionService.getAllTransactionDtos());
        model.addAttribute("transactionPage", transactionDtoPage);


        int totalPages = transactionDtoPage.getTotalPages();
        if (totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("contactList", myUserDetailsService.getUserContacts());
        model.addAttribute("errors", errors);

        return "transaction-form";
    }

    @PostMapping("/transaction")
    public String addTransaction(@ModelAttribute("transaction") TransactionDto transactionDto,
                                 BindingResult result){
        logger.info("calling method : addTransaction");
        transactionService.addTransaction(transactionDto, result);

        if (result.hasErrors()){
            logger.error("can't create transaction");
            errors = result.getAllErrors();
            return "redirect:/transaction?error";
        }

        logger.info("transaction success");
        return "redirect:/transaction?success";
    }

    @GetMapping("/transactions")
    public String showTransactionHistory(Model model){
        logger.info("calling method : showTransactionHistory");

        model.addAttribute("transactionsAsSender", transactionService.getTransactionAsSenderDtos());
        model.addAttribute("transactionsAsReceiver", transactionService.getTransactionAsReceiverDtos());

        return "transaction-history";
    }

}
