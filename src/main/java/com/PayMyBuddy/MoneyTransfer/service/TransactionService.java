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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.*;

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
        return transactionAmount * 0.005;
    }

    public void addTransaction(TransactionDto transactionDto, BindingResult result) {
        double transactionAmount = transactionDto.getAmount();
        if (transactionAmount <= 0.) {
            logger.error("amount can't be null");
            result.reject("transaction", "transaction can't be null");
            return;
        }

        User sender = myUserDetailsService.findUser();
        boolean isInContactList = false;

        for(User contact : sender.getContacts()){
            if(Objects.equals(contact.getEmail(), transactionDto.getReceiverEmail())) {
                isInContactList = true;
                break;
            }
        }

        if(!isInContactList){
            result.reject("transaction", "this email is not in the contact list");
            logger.error("this email is not in the contact list");
            return;
        }

        Optional<User> optionalReceiver = myUserDetailsService.findByEmail(transactionDto.getReceiverEmail());

        if (optionalReceiver.isEmpty()){
            result.reject("transaction", "there is not account with this email");
            logger.error("there is not account with this email");
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
                transactionCurrencyCode, senderCurrencyCode, transactionAmount + fee));
        myUserDetailsService.save(sender);

        receiver.setBalance(receiver.getBalance() + CurrencyConverter.convert(
                transactionCurrencyCode, receiverCurrencyCode, transactionAmount
        ));
        myUserDetailsService.save(receiver);
    }


    public ArrayList<TransactionDto> getTransactionAsSenderDtos() {
        ArrayList<TransactionDto> result = new ArrayList<>();

        myUserDetailsService.findUser().getTransactionsAsSender().forEach(
                transaction -> result.add(transactionMapper.toDto(transaction))
        );

        return result;
    }

    public ArrayList<TransactionDto> getTransactionAsReceiverDtos() {
        ArrayList<TransactionDto> result = new ArrayList<>();

        myUserDetailsService.findUser().getTransactionsAsReceiver().forEach(
                transaction -> result.add(transactionMapper.toDto(transaction))
        );

        return result;
    }

    public Page<TransactionDto> findPaginated(Pageable pageable, List<TransactionDto> transactionDtos){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<TransactionDto> list;

        if(transactionDtos.size() < startItem)
            list = Collections.emptyList();
        else{
            int toIndex = Math.min(startItem + pageSize, transactionDtos.size());
            list = transactionDtos.subList(startItem, toIndex);
            Collections.sort(list);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), transactionDtos.size());
    }

    public List<TransactionDto> getAllTransactionDtos() {
        ArrayList<TransactionDto> result = new ArrayList<>();
        result.addAll(getTransactionAsReceiverDtos());
        result.addAll(getTransactionAsSenderDtos());
        Collections.sort(result);

        return result;
    }
}

