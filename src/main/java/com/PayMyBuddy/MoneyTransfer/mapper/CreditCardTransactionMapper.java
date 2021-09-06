package com.PayMyBuddy.MoneyTransfer.mapper;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardTransactionDto;
import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import com.PayMyBuddy.MoneyTransfer.model.CreditCardTransaction;
import com.PayMyBuddy.MoneyTransfer.model.User;
import org.springframework.stereotype.Component;

@Component
public class CreditCardTransactionMapper {

    public CreditCardTransactionDto toDto(CreditCardTransaction creditCardTransaction){
        return new CreditCardTransactionDto(creditCardTransaction.getAmount(), creditCardTransaction.getCurrencyCode(), creditCardTransaction.getCreditCard().getCardNumber());
    }

    public CreditCardTransaction toEntity(CreditCardTransactionDto creditCardTransactionDto, User user, CreditCard creditCard) {
        CreditCardTransaction result = new CreditCardTransaction();

        result.setId(10);
        result.setAmount(creditCardTransactionDto.getAmount());
        result.setUser(user);
        result.setCurrencyCode(creditCardTransactionDto.getCurrencyCode());
        result.setCreditCard(creditCard);

        return result;
    }
}
