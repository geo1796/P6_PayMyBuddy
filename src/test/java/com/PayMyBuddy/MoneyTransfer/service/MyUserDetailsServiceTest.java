package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import com.PayMyBuddy.MoneyTransfer.dto.UserRegistrationDto;
import com.PayMyBuddy.MoneyTransfer.mapper.ContactMapper;
import com.PayMyBuddy.MoneyTransfer.model.Role;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.RoleRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    private ContactMapper contactMapper;


    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    private User user;
    private ContactDto contact;
    private User usersContact;
    private HashSet<User> usersContacts;
    private BindingResult result;
    private UserRegistrationDto registrationDto;


    @BeforeAll
    public void setup(){
        user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("user123");

        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(user.getEmail());
        registrationDto.setPassword(user.getPassword());

        usersContacts = new HashSet<>();
        contact = new ContactDto();
        contact.setEmail("contact@mail.com");

        usersContact = new User();
        usersContact.setEmail(contact.getEmail());
        usersContacts.add(usersContact);
        user.setContacts(usersContacts);
        Role role = new Role("ROLE_USER");
        Set<Role> usersRole = new HashSet<>();
        usersRole.add(role);
        user.setRoles(usersRole);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @BeforeEach
    public void eachSetup() {
        result = new BeanPropertyBindingResult(contact, "contact");
        user.setContacts(usersContacts);
    }

    @Test
    public void testAddYourselfInContactList(){
        ContactDto self = new ContactDto();
        self.setEmail(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        myUserDetailsService.addContact(self, result);

        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddContactAlreadyInContactList() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(usersContact.getEmail())).thenReturn(Optional.of(usersContact));

        myUserDetailsService.addContact(contact, result);

        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddNotExistingContact() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(usersContact.getEmail())).thenReturn(Optional.empty());
        myUserDetailsService.addContact(contact, result);

        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddContactSuccess() {
        ContactDto newContactDto = new ContactDto();
        newContactDto.setEmail("newContactDto@mail.com");
        User newContact = new User();
        newContact.setEmail(newContactDto.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(newContact.getEmail())).thenReturn(Optional.of(newContact));
        myUserDetailsService.addContact(newContactDto, result);

        assertFalse(result.hasErrors());
    }

    @Test
    public void testGetUserContacts() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(contactMapper.toDto(usersContact)).thenReturn(contact);
        List<ContactDto> expected = new ArrayList<>();
        expected.add(contact);
        List<ContactDto> actual = (List<ContactDto>) myUserDetailsService.getUserContacts();

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveAlreadyRegisteredUser(){
        when(userRepository.findByEmail(registrationDto.getEmail())).thenReturn(Optional.of(user));
        myUserDetailsService.save(registrationDto, result);
        assertTrue(result.hasErrors());
    }

    @Test
    public void testSaveUserWithWrongConfirmEmail(){
        when(userRepository.findByEmail(registrationDto.getEmail())).thenReturn(Optional.empty());
        registrationDto.setConfirmEmail("wrong");
        myUserDetailsService.save(registrationDto, result);
        assertTrue(result.hasErrors());
    }

    @Test
    public void testSaveUserWithWrongConfirmPassword(){
        registrationDto.setConfirmEmail(registrationDto.getEmail());
        registrationDto.setConfirmPassword("wrong");
        when(userRepository.findByEmail(registrationDto.getEmail())).thenReturn(Optional.empty());
        myUserDetailsService.save(registrationDto, result);
        assertTrue(result.hasErrors());
    }

    @Test
    public void testLoadNonRegisteredUser(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> myUserDetailsService.loadUserByUsername(user.getEmail()));
    }

    @Test
    public void testLoadRegisteredUser(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails result = myUserDetailsService.loadUserByUsername(user.getEmail());
        assertEquals(result.getUsername(), user.getEmail());
        assertEquals(result.getPassword(), user.getPassword());
    }
}
