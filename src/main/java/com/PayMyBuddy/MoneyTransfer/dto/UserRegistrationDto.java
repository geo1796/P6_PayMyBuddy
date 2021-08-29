package com.PayMyBuddy.MoneyTransfer.dto;

import com.PayMyBuddy.MoneyTransfer.constraints.FieldMatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@RequiredArgsConstructor
@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
        @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
})
public class UserRegistrationDto {

    @Email
    private String email;
    @Email
    private String confirmEmail;
    @NotEmpty
    private String password;
    @NotEmpty
    private String confirmPassword;

}
