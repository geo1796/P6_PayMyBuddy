package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardDto;
import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CreditCardControllerIT {

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowCreditCards() throws Exception{
        mockMvc.perform(get("/creditCards"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("creditCards", hasItem(hasProperty("cardNumber", equalTo("0000000000000000")))));
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
        CreditCardDto creditCard = new CreditCardDto();
        creditCard.setCardNumber("0000111122223333");
        creditCard.setLastName("doe");
        creditCard.setFirstName("john");
        creditCard.setExpirationDate("2022-01-01");

        mockMvc.perform(post("/addCreditCard")
                        .flashAttr("creditCard", creditCard))
                .andExpect(redirectedUrl("/addCreditCard?success"));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testAddAlreadyAddedCreditCard() throws Exception{
        CreditCardDto creditCard = new CreditCardDto();
        creditCard.setCardNumber("0000000000000000");
        creditCard.setLastName("doe");
        creditCard.setFirstName("john");
        creditCard.setExpirationDate("2025-01-01");

        mockMvc.perform(post("/addCreditCard")
                        .flashAttr("creditCard", creditCard))
                .andExpect(redirectedUrl("/addCreditCard?error"));
    }

}
