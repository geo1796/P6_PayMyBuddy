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
        Date thisDate = getEndDate();
        Date otherDate = o.getEndDate();

        if (thisDate.before(otherDate))
            return 1;
        else if (otherDate.before(thisDate))
            return -1;

        return 0;
    }
}
