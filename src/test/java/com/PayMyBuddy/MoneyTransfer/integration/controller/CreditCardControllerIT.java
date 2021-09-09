package com.PayMyBuddy.MoneyTransfer.integration.controller;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardDto;
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
public class CreditCardControllerIT {

    @Autowired
    MockMvc mockMvc;

    private CreditCardDto creditCard;

    @BeforeAll
    public void init(){
        creditCard = new CreditCardDto();
        creditCard.setLastName("Doe");
        creditCard.setFirstName("John");
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowCreditCards() throws Exception{
        creditCard.setCardNumber("0000000000000000");
        creditCard.setExpirationDate("2025-01-01");

        mockMvc.perform(get("/creditCards"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("creditCards", hasItem(samePropertyValuesAs(creditCard))));
    }

    @WithMockUser
    @Test
    public void testShowCreditCardForm() throws Exception{
        mockMvc.perform(get("/addCreditCard"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testAddCreditCard() throws Exception{
        creditCard.setCardNumber("0000111122223333");
        creditCard.setExpirationDate("2022-01-01");

        mockMvc.perform(post("/addCreditCard")
                        .flashAttr("creditCard", creditCard))
                .andExpect(redirectedUrl("/addCreditCard?success"));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testAddAlreadyAddedCreditCard() throws Exception{
        creditCard.setCardNumber("0000000000000000");
        creditCard.setExpirationDate("2025-01-01");

        mockMvc.perform(post("/addCreditCard")
                        .flashAttr("creditCard", creditCard))
                .andExpect(redirectedUrl("/addCreditCard?error"));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testAddExpiredCreditCard() throws Exception{
        creditCard.setCardNumber("1111222233334444");
        creditCard.setExpirationDate("2019-01-01");

        mockMvc.perform(post("/addCreditCard")
                        .flashAttr("creditCard", creditCard))
                .andExpect(redirectedUrl("/addCreditCard?error"));
    }

}
