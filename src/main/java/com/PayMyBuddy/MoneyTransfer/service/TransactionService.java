package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.mapper.TransactionMapper;
import com.PayMyBuddy.MoneyTransfer.model.Transaction;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.TransactionRepository;
import com.PayMyBuddy.MoneyTransfer.util.CurrencyConverter;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TransactionService {

    private static final Logger logger = LogManager.getLogger("TransactionService");
    private MyUserDetailsService myUserDetailsService;
    private TransactionRepository transactionRepository;
    private TransactionMapper transactionMapper;

    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    private double calculateFee(double transactionAmount) {
        return transactionAmount * 0.02;
    }

    public void addTransaction(TransactionDto transactionDto, BindingResult result) {
        double transactionAmount = transactionDto.getAmount();
        if (transactionAmount <= 0.) {
            logger.error("transaction can't be null");
            result.reject("transaction", "transaction can't be null");
            return;
        }

        User sender = myUserDetailsService.findUser();
        Optional<User> optionalReceiver = myUserDetailsService.findByEmail(transactionDto.getReceiverEmail());

        if (optionalReceiver.isEmpty()) {
            logger.error("No account for this email");
            result.reject("transaction", "No account for this email");
            return;
        }

        User receiver = optionalReceiver.get();

        double fee = calculateFee(transactionAmount);
        String transactionCurrencyCode = transactionDto.getCurrencyCode();
        String senderCurrencyCode = sender.getBalanceCurrencyCode();
        String receiverCurrencyCode = receiver.getBalanceCurrencyCode();

        if (CurrencyConverter.convert(senderCurrencyCode, transactionCurrencyCode, sender.getBalance()) < transactionAmount + fee) {
            result.reject("transaction", "Not enough money on this account");
            logger.error("Not enough money on this account");
            return;
        }

        Transaction transaction = new Transaction();
        //saving transaction
        transaction.setStartDate(new Date(Instant.now().toEpochMilli()));

        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(transactionAmount);
        transaction.setFee(fee);
        transaction.setDescription(transactionDto.getDescription());
        transaction.setCurrencyCode(transactionCurrencyCode);

        transaction.setEndDate(new Date(Instant.now().toEpochMilli()));

        transactionRepository.save(transaction);

        //updating users balances
        sender.setBalance(sender.getBalance() - CurrencyConverter.convert(
                senderCurrencyCode, transactionCurrencyCode, transactionAmount + fee));
        myUserDetailsService.save(sender);

        receiver.setBalance(receiver.getBalance() + CurrencyConverter.convert(
                transactionCurrencyCode, receiverCurrencyCode, transactionAmount
        ));
        myUserDetailsService.save(receiver);
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
