package com.PayMyBuddy.MoneyTransfer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "Bank_account_transaction")
@DynamicUpdate
@Getter
@Setter
@AllArgsConstructor
public class BankAccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "amount")
    private double amount;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "to_balance") // true means the money is sent from the bank account
    private Boolean toBalance;   // to the PayMyBuddy account and false means the opposite

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "iban")
    private BankAccount bankAccount;

    public BankAccountTransaction(){}

}
