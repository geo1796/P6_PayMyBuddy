package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserRegistrationDto {

    private String email;
    private String password;
    private String matchingPassword;

}
