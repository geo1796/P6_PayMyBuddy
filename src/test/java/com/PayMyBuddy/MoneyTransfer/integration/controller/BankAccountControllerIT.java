package com.PayMyBuddy.MoneyTransfer.integration.controller;

import com.PayMyBuddy.MoneyTransfer.model.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BankAccountControllerIT {

    @Autowired
    MockMvc mockMvc;

    private BankAccount bankAccount;

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowBankAccounts() throws Exception{
        mockMvc.perform(get("/bankAccounts"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bankAccounts", hasItem(hasProperty("iban", equalTo("ibanTest")))));
    }

    @WithMockUser(username = "son.goku@mail.com")
    @Test
    public void testShowBankAccountForm() throws Exception{
        mockMvc.perform(get("/addBankAccount"))
                .andExpect(status().isOk());
    }

    @WithMockUser("son.goku@mail.com")
    @Test
    public void testAddBankAccountSuccess() throws Exception{
        bankAccount = new BankAccount();
        bankAccount.setIban("SonGokusAccount");
        bankAccount.setCustomerId(123);
        bankAccount.setPassword("123");

        mockMvc.perform(post("/addBankAccount")
                .flashAttr("bankAccount", bankAccount))
                .andExpect(redirectedUrl("/addBankAccount?success"));
    }

    @WithMockUser("john.doe@mail.com")
    @Test
    public void testAddBankAccountError() throws Exception{
        bankAccount = new BankAccount();
        bankAccount.setIban("ibanTest");
        bankAccount.setCustomerId(123);
        bankAccount.setPassword("123");

        mockMvc.perform(post("/addBankAccount")
                        .flashAttr("bankAccount", bankAccount))
                .andExpect(redirectedUrl("/addBankAccount?error"));
    }

}
