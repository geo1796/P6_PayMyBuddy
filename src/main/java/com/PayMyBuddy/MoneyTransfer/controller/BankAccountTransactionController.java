package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.service.BankAccountTransactionService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;


@Controller
@AllArgsConstructor
public class BankAccountTransactionController {

    private BankAccountTransactionService bankAccountTransactionService;
    private static final Logger logger = LogManager.getLogger("BankAccountTransactionController");


}
