package com.PayMyBuddy.MoneyTransfer.integration.controller;


import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MainControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }

    @WithMockUser(username = "john.doe@mail.com")
    @Test
    public void testShowIndex() throws Exception{
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    public void testUserLoginSuccess() throws Exception {
        mockMvc.perform(formLogin("/login").user("son.goku@mail.com").password("goku123")).andExpect(authenticated());
    }

    @Test
    public void testUserLoginFailure() throws Exception {
        mockMvc.perform(formLogin("/login").user("springuser").password("wrongpassword")).andExpect(unauthenticated());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void testGetAdminPage() throws Exception{
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void testGetAdminShouldBeForbidden() throws Exception{
        mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
    }

}
