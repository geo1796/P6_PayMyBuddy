package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.PayMyBuddy.MoneyTransfer.util.Json.stringify;
import static com.PayMyBuddy.MoneyTransfer.util.Json.toJson;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    MockMvc mockMvc;


    @WithMockUser
    @Test
    public void testGetUser() throws Exception {
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john.doe@mail.com")));
    }

    @WithMockUser
    @Test
    public void testGetNotExistingUser() throws Exception {
        mockMvc.perform(get("/user/0"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    public void testGetAllUsers() throws Exception{
        mockMvc.perform(get("/allUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].email", is("john.doe@mail.com")));
    }

    @WithMockUser
    @Test
    public void testGetContact() throws Exception{
        mockMvc.perform(get("/user/1/contacts/son.goku@mail.com"))
                .andExpect(jsonPath("$.email", is("son.goku@mail.com")));
    }

    @WithMockUser
    @Test
    public void testAddContact() throws Exception{
        ContactDto contactDto = new ContactDto();
        contactDto.setEmail("john.doe@mail.com");
        mockMvc.perform(put("/user/2/contacts")
                        .contentType(MediaType.APPLICATION_JSON).content(stringify(toJson(contactDto))))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void testSendMoneyToBankAccount() throws Exception{

    }

}
