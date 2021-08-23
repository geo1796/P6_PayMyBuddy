package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.UserRegistrationDto;
import com.PayMyBuddy.MoneyTransfer.model.Role;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.model.UserRole;
import com.PayMyBuddy.MoneyTransfer.repository.RoleRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Iterable<User> getUsers(){ return userRepository.findAll(); }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        User user = optionalUser.get();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection <Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public Optional<User> findByEmail(String email) {return userRepository.findByEmail(email);}

    public User save(UserRegistrationDto registrationDto) {
        User newUser = new User(registrationDto.getEmail(), passwordEncoder.encode(registrationDto.getPassword()));
        newUser = userRepository.save(newUser);
        userRoleRepository.save(new UserRole(newUser.getId(), roleRepository.findByName("ROLE_USER").getId()));

        return newUser;
    }
}
