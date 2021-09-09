package com.PayMyBuddy.MoneyTransfer.integration.controller;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import org.junit.jupiter.api.BeforeEach;
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
public class UserControllerIT {

    @Autowired
    MockMvc mockMvc;

    private ContactDto contactDto;

    @BeforeEach
    public void init(){ this.contactDto = new ContactDto(); }

    @WithMockUser(username = "son.goku@mail.com")
    @Test
    public void testShowNewContactForm() throws Exception{
        mockMvc.perform(get("/addContact"))
                .andExpect(status().isOk())
                .andExpect(view().name("new-contact-form"));
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowContactList() throws Exception{
        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("contactList", hasItem(hasProperty("email", equalTo("son.goku@mail.com")))));
    }

    @WithMockUser(username = "son.goku@mail.com")
    @Test
    public void testAddNotExistingContact() throws Exception{
        contactDto.setEmail("notExisting@mail.com");
        mockMvc.perform(post("/addContact")
                .flashAttr("contact", contactDto))
                .andExpect(redirectedUrl("/addContact?error"));
    }

    @WithMockUser(username = "son.goku@mail.com")
    @Test
    public void testAddContact() throws Exception {

        contactDto.setEmail("john.doe@mail.com");
        mockMvc.perform(post("/addContact").
                        flashAttr("contact", contactDto))
                .andExpect(redirectedUrl("/addContact?success"));
    }
}
