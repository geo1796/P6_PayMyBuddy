package com.PayMyBuddy.MoneyTransfer.dto;

import com.PayMyBuddy.MoneyTransfer.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@Setter
public class TransactionDto {

    @NotNull
    private int id;
    @NotNull
    private double amount;
    @NotNull
    private double fee;
    @NotEmpty
    private String currencyCode;
    @NotEmpty
    private Date startDate;
    @NotEmpty
    private Date endDate;
    @NotEmpty
    private String description;
    @Email
    @NotEmpty
    private String senderEmail;
    @Email
    @NotEmpty
    private String receiverEmail;

}
