package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Generated
@AllArgsConstructor
@Getter
@Setter
public class CreditCardTransactionDto {

    private double amount;

    private String currencyCode;

    private String cardNumber;

    public CreditCardTransactionDto(){}

}
