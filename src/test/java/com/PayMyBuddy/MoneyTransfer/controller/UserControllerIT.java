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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(username = "son.goku@mail.com")
    @Test
    public void testAddContact() throws Exception {
        ContactDto contactDto = new ContactDto();
        contactDto.setEmail("john.doe@mail.com");
        mockMvc.perform(post("/addContact").
                        flashAttr("contact", contactDto))
                .andExpect(redirectedUrl("/addContact?success"));
    }

}
