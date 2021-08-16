package com.PayMyBuddy.MoneyTransfer.Service;

import com.PayMyBuddy.MoneyTransfer.Repository.CreditCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreditCardService {

    private CreditCardRepository creditCardRepository;

}
