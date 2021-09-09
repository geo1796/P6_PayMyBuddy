package com.PayMyBuddy.MoneyTransfer.integration.controller;

import com.PayMyBuddy.MoneyTransfer.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
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

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/registration")).andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void testRegisterUserAccountSuccess() throws Exception{
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("user@gmail.com");
        userRegistrationDto.setConfirmEmail("user@gmail.com");
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setConfirmPassword("password");

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
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("son.goku@mail.com");
        userRegistrationDto.setConfirmEmail("son.goku@mail.com");
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setConfirmPassword("password");

        mockMvc.perform(post("/registration")
                .flashAttr("user", userRegistrationDto))
                .andExpect(redirectedUrl("/registration?error"));
    }

    @WithMockUser
    @Test
    public void testRegistrationUserAccountWithWrongConfirmEmail() throws Exception{
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("user1@mail.com");
        userRegistrationDto.setConfirmEmail("user2@mail.com");
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setConfirmPassword("password");

        mockMvc.perform(post("/registration")
                .flashAttr("user", userRegistrationDto))
                .andExpect(redirectedUrl("/registration?error"));
    }

    @WithMockUser
    @Test
    public void testRegistrationUserAccountWithWrongConfirmPassword() throws Exception{
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("user1@mail.com");
        userRegistrationDto.setConfirmEmail("user1@mail.com");
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setConfirmPassword("password123");

        mockMvc.perform(post("/registration")
                .flashAttr("user", userRegistrationDto))
                .andExpect(redirectedUrl("/registration?error"));
    }


}
