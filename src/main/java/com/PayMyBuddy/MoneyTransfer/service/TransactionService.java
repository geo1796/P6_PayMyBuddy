package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.model.Transaction;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@AllArgsConstructor
@Service
public class TransactionService {

    private MyUserDetailsService myUserDetailsService;
    private TransactionRepository transactionRepository;

    public Iterable<Transaction> getTransactions(){ return transactionRepository.findAll(); }

    private double calculateFee(double transactionAmount) { return transactionAmount * 0.05; }

    public void addTransaction(TransactionDto transactionDto, BindingResult result) {
        User user = myUserDetailsService.findUser();

        double transactionAmount = transactionDto.getAmount();
        double fee = calculateFee(transactionAmount);

        if (user.getBalance() < transactionAmount + fee)
            result.rejectValue("amount", null, "Not enough money on this account !");
        else {
            Transaction transaction = new Transaction();
            transaction.setSender(user);
            transaction.setReceiver(myUserDetailsService.findByEmail(transactionDto.getReceiverEmail()).get());
            transaction.setAmount(transactionAmount);
            transaction.setFee(fee);
            transaction.setDescription(transactionDto.getDescription());
            transaction.setStartDate(transactionDto.getStartDate());
            transaction.setEndDate(transactionDto.getEndDate());
            transaction.setCurrencyCode(transactionDto.getCurrencyCode());

            transactionRepository.save(transaction);
        }
    }


}
