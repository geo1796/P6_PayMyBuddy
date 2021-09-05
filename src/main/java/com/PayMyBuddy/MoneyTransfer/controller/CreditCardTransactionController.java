package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.service.CreditCardTransactionService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class CreditCardTransactionController {

    private static final Logger logger = LogManager.getLogger("CreditCardTransactionController");
    private CreditCardTransactionService creditCardTransactionService;

}
