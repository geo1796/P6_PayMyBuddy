package com.PayMyBuddy.MoneyTransfer.mapper;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.model.Transaction;
import com.PayMyBuddy.MoneyTransfer.util.Currency;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction){
        TransactionDto result = new TransactionDto();

        result.setReceiverEmail(transaction.getReceiver().getEmail());
        result.setSenderEmail(transaction.getSender().getEmail());
        result.setAmount(transaction.getAmount());
        result.setCurrencyCode(Currency.valueOf(transaction.getCurrencyCode()).symbol); //to display the symbol of the currency
        result.setEndDate(transaction.getEndDate());
        result.setDescription(transaction.getDescription());

        return result;
    }

}
