package com.PayMyBuddy.MoneyTransfer.mapper;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardDto;
import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class CreditCardMapper {

    public CreditCard toEntity(CreditCardDto creditCardDto){
        CreditCard result = new CreditCard();
        result.setFirstName(creditCardDto.getFirstName());
        result.setLastName(creditCardDto.getLastName());
        result.setCardNumber(creditCardDto.getCardNumber());
        result.setExpirationDate(Date.valueOf(creditCardDto.getExpirationDate()));

        return result;
    }

}
