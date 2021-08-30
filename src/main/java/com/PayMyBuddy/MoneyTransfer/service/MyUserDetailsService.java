package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import com.PayMyBuddy.MoneyTransfer.dto.UserDto;
import com.PayMyBuddy.MoneyTransfer.dto.UserRegistrationDto;
import com.PayMyBuddy.MoneyTransfer.mapper.ContactMapper;
import com.PayMyBuddy.MoneyTransfer.mapper.UserMapper;
import com.PayMyBuddy.MoneyTransfer.model.Role;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.model.UserRole;
import com.PayMyBuddy.MoneyTransfer.repository.RoleRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ContactMapper contactMapper;

    public Iterable<User> getAllUsers(){ return userRepository.findAll(); }

    public Optional<User> findById(int id){ return userRepository.findById(id); }

    public User findUser() {
        String userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userMail).get();
    }

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

    public Optional<UserDto> getUserDto(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> userMapper.toDto(user));
    }

    public Optional<UserDto> getUserDto(String contactEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(contactEmail);
        return optionalUser.map(user -> userMapper.toDto(user));
    }

    public Iterable<UserDto> getAllUserDtos(){
        ArrayList<UserDto> result = new ArrayList<>();

        userRepository.findAll().forEach(
                user -> result.add(userMapper.toDto(user))
        );

        return result;
    }


    public void addContact(ContactDto contactDto) {
        User user = findUser();

            String contactEmail = contactDto.getEmail();
            Optional<User> optionalContactUser = userRepository.findByEmail(contactEmail);
            if (optionalContactUser.isPresent()){
                User newContact = optionalContactUser.get();
                List<User> contacts = user.getContacts();
                if(contacts.contains(newContact))
                    throw new IllegalArgumentException("This person is already in the contact list");
                else {
                    contacts.add(newContact);
                    userRepository.save(user);
                }
            }
            else
                throw new IllegalArgumentException("There is no user with email : " + contactEmail);
        }


    public Optional<ContactDto> findUserContact(UserDto userDto, String contactEmail) {
        for(ContactDto contactDto : userDto.getContacts()){
            if(Objects.equals(contactDto.getEmail(), contactEmail))
                return Optional.of(contactDto);
        }
        return Optional.empty();
    }

    public Optional<UserDto> findUserDtoById(int id) {
        Optional<User> optionalUser = findById(id);
        return optionalUser.map(user -> userMapper.toDto(user));
    }

    public Iterable<ContactDto> getUserContacts() {
        ArrayList<ContactDto> result = new ArrayList<>();
        findUser().getContacts().forEach(
                contact -> result.add(contactMapper.toDto(contact))
        );

        return result;
    }
}
