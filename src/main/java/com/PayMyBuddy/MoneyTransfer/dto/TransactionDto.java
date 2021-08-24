package com.PayMyBuddy.MoneyTransfer.dto;

import com.PayMyBuddy.MoneyTransfer.model.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class TransactionDto {

    private double amount;

    private double fee;

    private String currencyCode;

    private Date startDate;

    private Date endDate;

    private String description;

    private User sender;

    private User receiver;

}
