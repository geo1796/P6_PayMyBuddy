package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Generated
public class TransactionDto implements Comparable<TransactionDto> {

    private double amount;

    private String currencyCode;

    private String description = "None";

    private String receiverEmail;

    private String senderEmail;

    private Date endDate;

    @Override
    public int compareTo(TransactionDto o) {
        return - getEndDate().compareTo(o.getEndDate());
    }
}
