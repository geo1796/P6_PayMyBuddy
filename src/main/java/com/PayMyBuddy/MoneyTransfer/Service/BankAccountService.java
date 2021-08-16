package com.PayMyBuddy.MoneyTransfer.Service;

import com.PayMyBuddy.MoneyTransfer.Repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

}
