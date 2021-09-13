package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
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
public class TransactionControllerIT {

    @Autowired
    MockMvc mockMvc;

    private TransactionDto transactionDto;

    @BeforeAll
    public void init(){
        transactionDto = new TransactionDto();
        transactionDto.setCurrencyCode("GBP");
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowTransactionForm() throws Exception{
        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("contactList", hasItem(hasProperty("email", equalTo("son.goku@mail.com")))));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowTransactionHistory() throws Exception{
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("transactionsAsSender", hasItem(hasProperty("description", equalTo("From John Doe to Son Goku")))))
                .andExpect(model().attribute("transactionsAsReceiver", hasItem(hasProperty("description", equalTo("From Son Goku to John Doe")))));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testAddTransaction() throws Exception{
        transactionDto.setReceiverEmail("son.goku@mail.com");
        transactionDto.setAmount(1.);
        mockMvc.perform(post("/transaction")
                .flashAttr("transaction", transactionDto))
                .andExpect(redirectedUrl("/transaction?success"));
    }

    @WithMockUser(username = "son.goku@mail.com")
    @Test
    public void testAddTransactionWithoutEnoughMoney() throws Exception{
        transactionDto.setReceiverEmail("john.doe@mail.com");
        transactionDto.setAmount(200);
        mockMvc.perform(post("/transaction")
                        .flashAttr("transaction", transactionDto))
                .andExpect(redirectedUrl("/transaction?error"));
    }

}
