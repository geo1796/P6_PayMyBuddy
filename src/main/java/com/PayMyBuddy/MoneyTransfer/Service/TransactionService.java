package com.PayMyBuddy.MoneyTransfer.Service;

import com.PayMyBuddy.MoneyTransfer.Model.Transaction;
import com.PayMyBuddy.MoneyTransfer.Model.User;
import com.PayMyBuddy.MoneyTransfer.Repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public Iterable<Transaction> getTransactions(){ return transactionRepository.findAll(); }

}
