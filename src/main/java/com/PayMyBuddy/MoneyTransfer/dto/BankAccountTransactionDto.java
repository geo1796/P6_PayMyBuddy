package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Generated
@AllArgsConstructor
public class BankAccountTransactionDto {

    private double amount;

    private String currencyCode;

    private String iban;

    private Boolean toBalance;

    public BankAccountTransactionDto(){}

}
