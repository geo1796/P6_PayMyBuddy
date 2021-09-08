package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.UserDto;
import com.PayMyBuddy.MoneyTransfer.model.Role;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    private User user;
    private Role role;
    private UserDto userDto;

    private Collection < ? extends GrantedAuthority> mapRolesToAuthorities(Collection <Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @BeforeAll
    public void init(){
        role = new Role();
        role.setName("ROLE_USER");
        Set<Role> roleCollection = new HashSet<>(Collections.emptySet());
        roleCollection.add(role);

        user = new User();
        user.setRoles(roleCollection);
        user.setEmail("user@mail.com");
        user.setPassword("user123");

        userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());

    }

    @Test
    public void testLoadByUsername(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails expected = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
        UserDetails actual = myUserDetailsService.loadUserByUsername(user.getEmail());

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveUser(){

    }

}
