package com.PayMyBuddy.MoneyTransfer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "balance")
    private double balance;

    @Column(name = "balance_currency_code")
    private String balanceCurrencyCode;

    @OneToMany(
            mappedBy = "sender"
    )
    private List<Transaction> transactionsAsSender;

    @OneToMany(
            mappedBy = "receiver"
    )
    private List<Transaction> transactionsAsReceiver;

    @OneToMany
    @JoinTable(
            name = "User_contact",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private List<User> contacts;

    @ManyToMany
    @JoinTable(
            name = "User_credit_card",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "card_number")
    )
    private List<CreditCard> creditCards;

    @ManyToMany
    @JoinTable(
            name = "User_bank_account",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "iban")
    )
    private List<BankAccount> bankAccounts;

    @ManyToMany(
            fetch = FetchType.EAGER, cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "User_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    public User(String email, String password, Collection<Role> roles){
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(){};
}
