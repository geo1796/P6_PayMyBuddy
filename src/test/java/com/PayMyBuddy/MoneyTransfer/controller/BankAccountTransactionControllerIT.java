package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountTransactionDto;
import com.PayMyBuddy.MoneyTransfer.model.BankAccountTransaction;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankAccountTransactionControllerIT {

    @Autowired
    MockMvc mockMvc;

    private BankAccountTransactionDto bankAccountTransactionDto;
    private BankAccountTransaction bankAccountTransaction;

    @BeforeAll
    public void init(){
        bankAccountTransactionDto = new BankAccountTransactionDto();
        bankAccountTransaction = new BankAccountTransaction();
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowBankAccountsTransactions() throws Exception {
        bankAccountTransactionDto.setAmount(10);
        bankAccountTransactionDto.setIban("ibanTest");
        bankAccountTransactionDto.setCurrencyCode("€");
        bankAccountTransactionDto.setToBalance(false);
        mockMvc.perform(get("/bankAccountTransactions"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bankAccountTransactions", hasItem(samePropertyValuesAs(bankAccountTransactionDto))));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowBankAccountTransactionForm() throws Exception {
        mockMvc.perform(get("/bankAccountTransaction"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testFromBankAccountToBalance() throws Exception {
        bankAccountTransactionDto.setToBalance(true);
        bankAccountTransactionDto.setCurrencyCode("EUR");
        bankAccountTransactionDto.setAmount(10);
        bankAccountTransactionDto.setIban("ibanTest");


        mockMvc.perform(post("/bankAccountTransaction")
                .flashAttr("bankAccountTransaction", bankAccountTransactionDto))
                .andExpect(redirectedUrl("/bankAccountTransaction?success"));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testFromBalanceToBankAccount() throws Exception {
        bankAccountTransactionDto.setToBalance(false);
        bankAccountTransactionDto.setCurrencyCode("GBP");
        bankAccountTransactionDto.setAmount(1);
        bankAccountTransactionDto.setIban("ibanTest");

        mockMvc.perform(post("/bankAccountTransaction")
                        .flashAttr("bankAccountTransaction", bankAccountTransactionDto))
                .andExpect(redirectedUrl("/bankAccountTransaction?success"));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testAddBankAccountTransactionWithNotEnoughMoney() throws Exception{
        bankAccountTransactionDto.setToBalance(false);
        bankAccountTransactionDto.setCurrencyCode("EUR");
        bankAccountTransactionDto.setAmount(10);
        bankAccountTransactionDto.setIban("ibanTest");

        mockMvc.perform(post("/bankAccountTransaction")
                        .flashAttr("bankAccountTransaction", bankAccountTransactionDto))
                .andExpect(redirectedUrl("/bankAccountTransaction?error"));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testFromBalanceToBankAccountWithNotEnoughMoney() throws Exception {
        bankAccountTransactionDto.setToBalance(false);
        bankAccountTransactionDto.setCurrencyCode("EUR");
        bankAccountTransactionDto.setAmount(1000);
        bankAccountTransactionDto.setIban("ibanTest");

        mockMvc.perform(post("/bankAccountTransaction")
                        .flashAttr("bankAccountTransaction", bankAccountTransactionDto))
                .andExpect(redirectedUrl("/bankAccountTransaction?error"));
    }
}
