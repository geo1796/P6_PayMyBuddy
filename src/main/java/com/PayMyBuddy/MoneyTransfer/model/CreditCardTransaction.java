package com.PayMyBuddy.MoneyTransfer.model;

import javax.persistence.*;

@Entity
@Table(name = "Credit_card_transaction")
public class CreditCardTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "amount")
    private double amount;

    @Column(name = "currency_code")
    private String currencyCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_number")
    private CreditCard creditCard;

}
