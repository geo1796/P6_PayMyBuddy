package com.PayMyBuddy.MoneyTransfer.integration.controller;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardTransactionDto;
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


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditCardTransactionControllerIT {

    @Autowired
    MockMvc mockMvc;

    private CreditCardTransactionDto creditCardTransactionDto;

    @BeforeAll
    public void setup(){
        creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setAmount(100);
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowCreditCardTransactions() throws Exception{
        mockMvc.perform(get("/creditCardTransactions"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("creditCardTransactions", hasItem(hasProperty("amount", equalTo(100.)))));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowCreditCardTransactionForm() throws Exception{
        mockMvc.perform(get("/creditCardTransaction"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testFromCreditCardToBalance() throws Exception{
        creditCardTransactionDto.setCardNumber("0000000000000000");
        creditCardTransactionDto.setCurrencyCode("EUR");

        mockMvc.perform(post("/creditCardTransaction")
                        .flashAttr("creditCardTransaction", creditCardTransactionDto))
                .andExpect(redirectedUrl("/creditCardTransaction?success"));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testFromUnknownCreditCardToBalance() throws Exception{
        creditCardTransactionDto.setCardNumber("123");
        mockMvc.perform(post("/creditCardTransaction")
                .flashAttr("creditCardTransaction", creditCardTransactionDto))
                .andExpect(redirectedUrl("/creditCardTransaction?error"));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testFromExpiredCreditCardToBalance() throws Exception{
        creditCardTransactionDto.setCardNumber("3333222211110000");
        mockMvc.perform(post("/creditCardTransaction")
                        .flashAttr("creditCardTransaction", creditCardTransactionDto))
                .andExpect(redirectedUrl("/creditCardTransaction?error"));
    }
}
