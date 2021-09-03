package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Generated
public class UserDto {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    private double balance;

    private String balanceCurrencyCode;

    private List<ContactDto> contacts;

    private List<TransactionDto> transactionsAsSender;

    private List<TransactionDto> transactionsAsReceiver;


}
