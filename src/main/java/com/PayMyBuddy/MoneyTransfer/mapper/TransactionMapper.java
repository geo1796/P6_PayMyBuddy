package com.PayMyBuddy.MoneyTransfer.mapper;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction){
        TransactionDto result = new TransactionDto();

        result.setReceiverEmail(transaction.getReceiver().getEmail());
        result.setSenderEmail(transaction.getSender().getEmail());
        result.setAmount(transaction.getAmount());
        result.setCurrencyCode(transaction.getCurrencyCode());
        result.setStartDate(transaction.getStartDate());
        result.setDescription(transaction.getDescription());

        return result;
    }

}
