package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.util.Date;

@Getter
@Setter
@Generated
public class TransactionDto {

    private double amount;

    private String currencyCode;

    private String description;

    @Email
    private String receiverEmail;

    private Date startDate;

    private Date endDate;

}
