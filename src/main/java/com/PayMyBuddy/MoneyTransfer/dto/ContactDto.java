package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ContactDto {

    @NotEmpty
    private String email;

}
