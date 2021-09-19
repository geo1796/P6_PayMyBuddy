package com.PayMyBuddy.MoneyTransfer.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@DynamicUpdate
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
    private Set<Transaction> transactionsAsSender;

    @OneToMany(
            mappedBy = "receiver"
    )
    private Set<Transaction> transactionsAsReceiver;

    @OneToMany(
            mappedBy = "user"
    )
    private Set<BankAccountTransaction> bankAccountTransactions;

    @OneToMany(
            mappedBy = "user"
    )
    private Set<CreditCardTransaction> creditCardTransactions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "User_contact",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private Set<User> contacts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_credit_card",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "card_number")
    )
    private Set<CreditCard> creditCards;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "User_bank_account",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "iban")
    )
    private Set<BankAccount> bankAccounts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User(){}
}
