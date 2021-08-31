package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.sql.Date;

@Getter
@Setter
public class TransactionDto {

    private double amount;

    private String currencyCode;

    private Date startDate;

    private Date endDate;

    private String description;

    @Email
    private String receiverEmail;

}
