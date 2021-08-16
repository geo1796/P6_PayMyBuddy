package com.PayMyBuddy.MoneyTransfer.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "Credit_card")
public class CreditCard {

    @Id
    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "expiration_date")
    private Date expirationDate;

}
