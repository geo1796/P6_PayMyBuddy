package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UserDto {

    @NotNull
    private int id;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotNull
    private double balance;
    @NotEmpty
    private String balanceCurrencyCode;
    @NotNull
    private List<ContactDto> contacts;
    @NotNull
    private List<TransactionDto> transactionsAsSender;
    @NotNull
    private List<TransactionDto> transactionsAsReceiver;


}
