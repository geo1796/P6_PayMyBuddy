package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.mapper.TransactionMapper;
import com.PayMyBuddy.MoneyTransfer.model.Transaction;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.TransactionRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;

@AllArgsConstructor
@Service
public class TransactionService {

    private MyUserDetailsService myUserDetailsService;
    private TransactionRepository transactionRepository;
    private TransactionMapper transactionMapper;

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

            transaction.setStartDate(new Date(Instant.now().toEpochMilli()));

            transaction.setSender(user);
            transaction.setReceiver(myUserDetailsService.findByEmail(transactionDto.getReceiverEmail()).get());
            transaction.setAmount(transactionAmount);
            transaction.setFee(fee);
            transaction.setDescription(transactionDto.getDescription());
            transaction.setCurrencyCode(transactionDto.getCurrencyCode());

            transaction.setEndDate(new Date(Instant.now().toEpochMilli()));

            transactionRepository.save(transaction);

            user.setBalance(user.getBalance() - (transactionAmount + fee) );
            myUserDetailsService.save(user);
        }
    }


    public ArrayList<TransactionDto> getTransactionAsSenderDtos() {
        User user = myUserDetailsService.findUser();
        ArrayList<TransactionDto> result = new ArrayList<>();

        user.getTransactionsAsSender().forEach(
                transaction -> result.add(transactionMapper.toDto(transaction))
        );

        return result;
    }

    public ArrayList<TransactionDto> getTransactionAsReceiverDtos() {
        User user = myUserDetailsService.findUser();
        ArrayList<TransactionDto> result = new ArrayList<>();

        user.getTransactionsAsReceiver().forEach(
                transaction -> result.add(transactionMapper.toDto(transaction))
        );

        return result;
    }
}
