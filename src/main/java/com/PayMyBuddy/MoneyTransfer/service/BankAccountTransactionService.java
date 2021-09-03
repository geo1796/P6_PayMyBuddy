package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.model.BankAccountTransaction;
import com.PayMyBuddy.MoneyTransfer.repository.BankAccountTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BankAccountTransactionService {

    private BankAccountTransactionRepository bankAccountTransactionRepository;

    public List<BankAccountTransaction> findAll() {
        return bankAccountTransactionRepository.findAll();
    }
}
