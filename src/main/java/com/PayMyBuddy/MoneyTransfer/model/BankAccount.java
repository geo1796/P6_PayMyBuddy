package com.PayMyBuddy.MoneyTransfer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "Bank_account")
public class BankAccount {

    @Id
    @Column(name = "iban")
    private String iban;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "password")
    private String password;

}
