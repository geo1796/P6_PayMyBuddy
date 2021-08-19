package com.PayMyBuddy.MoneyTransfer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "Authority")
@Getter
@Setter
public class Authority {

    @Id
    @Column(name = "user_id")
    private int user_id;

    @Column(name = "authority_name")
    private String authorityName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
