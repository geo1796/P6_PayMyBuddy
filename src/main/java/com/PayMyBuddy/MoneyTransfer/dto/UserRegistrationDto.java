package com.PayMyBuddy.MoneyTransfer.dto;

import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@RequiredArgsConstructor
@Generated
public class UserRegistrationDto {

    @Email
    private String email;
    @NotEmpty
    private String confirmEmail;
    @NotEmpty
    private String password;
    @NotEmpty
    private String confirmPassword;

}
