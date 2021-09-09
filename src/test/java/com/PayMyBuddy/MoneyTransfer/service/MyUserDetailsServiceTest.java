package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import com.PayMyBuddy.MoneyTransfer.model.Role;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.RoleRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;


    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    private User user;
    private ContactDto contact;
    private User usersContact;
    private HashSet<User> usersContacts;

    @BeforeAll
    public void setup(){

        user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("user123");

        usersContacts = new HashSet<>();
        contact = new ContactDto();
        contact.setEmail("contact@mail.com");

        usersContact = new User();
        usersContact.setEmail(contact.getEmail());
        usersContacts.add(usersContact);
        user.setContacts(usersContacts);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @BeforeEach
    public void eachSetup() {
        user.setContacts(usersContacts);
    }

    @Test
    public void testAddContactAlreadyInContactList() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(usersContact.getEmail())).thenReturn(Optional.of(usersContact));
        BindingResult result = new BeanPropertyBindingResult(contact, "contact");
        myUserDetailsService.addContact(contact, result);

        assertTrue(result.hasFieldErrors("email"));
    }

    @Test
    public void testAddNotExistingContact() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(usersContact.getEmail())).thenReturn(Optional.empty());
        BindingResult result = new BeanPropertyBindingResult(contact, "contact");
        myUserDetailsService.addContact(contact, result);

        assertTrue(result.hasFieldErrors("email"));
    }

    @Test
    public void testAddContactSuccess() {
        user.setContacts(new HashSet<>());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(usersContact.getEmail())).thenReturn(Optional.of(usersContact));
        BindingResult result = new BeanPropertyBindingResult(contact, "contact");
        myUserDetailsService.addContact(contact, result);

        assertFalse(result.hasErrors());
    }
}
