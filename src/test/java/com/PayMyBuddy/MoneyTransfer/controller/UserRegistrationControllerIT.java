package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc()
public class UserRegistrationControllerIT {

    @Autowired
    MockMvc mockMvc;

    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    public void setup(){
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("user@gmail.com");
        userRegistrationDto.setConfirmEmail("user@gmail.com");
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setConfirmPassword("password");
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/registration")).andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void testRegisterUserAccountSuccess() throws Exception{
        mockMvc.perform(post("/registration")
                .flashAttr("user", userRegistrationDto))
                .andExpect(redirectedUrl("/registration?success"));
    }

    @WithMockUser
    @Test
    public void testRegistrationUserAccountFailure() throws Exception{
        mockMvc.perform(post("/registration")
                .flashAttr("user", new UserRegistrationDto()))
                .andExpect(redirectedUrl("/registration?error"));
    }

    @WithMockUser
    @Test
    public void testRegistrationUserAccountWithAlreadyRegisteredEmail() throws Exception{
        userRegistrationDto.setEmail("john.doe@mail.com");
        userRegistrationDto.setConfirmEmail("john.doe@mail.com");
        mockMvc.perform(post("/registration")
                .flashAttr("user", userRegistrationDto))
                .andExpect(redirectedUrl("/registration?error"));
    }

    @WithMockUser
    @Test
    public void testRegistrationUserAccountWithWrongConfirmEmail() throws Exception{
        userRegistrationDto.setConfirmEmail("wrong");
        mockMvc.perform(post("/registration")
                .flashAttr("user", userRegistrationDto))
                .andExpect(redirectedUrl("/registration?error"));
    }

    @WithMockUser
    @Test
    public void testRegistrationUserAccountWithWrongConfirmPassword() throws Exception{
        userRegistrationDto.setConfirmPassword("wrong");
        mockMvc.perform(post("/registration")
                .flashAttr("user", userRegistrationDto))
                .andExpect(redirectedUrl("/registration?error"));
    }


}
