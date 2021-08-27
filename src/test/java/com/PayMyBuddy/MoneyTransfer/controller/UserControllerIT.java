package com.PayMyBuddy.MoneyTransfer.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
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
        mockMvc.perform(put("/user/2/contacts/john.doe@mail.com"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user/2/contacts/john.doe@mail.com"))
                .andExpect(jsonPath("$.email", is("john.doe@mail.com")));
    }

    @WithMockUser
    @Test
    @Order(1)
    public void test() throws Exception{
        mockMvc.perform(get("/user/2/contacts/john.doe@mail.com"))
                .andExpect(status().isNotFound());
    }

}
