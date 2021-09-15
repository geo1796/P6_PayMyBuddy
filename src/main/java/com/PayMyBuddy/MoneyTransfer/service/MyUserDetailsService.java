package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import com.PayMyBuddy.MoneyTransfer.dto.UserDto;
import com.PayMyBuddy.MoneyTransfer.dto.UserRegistrationDto;
import com.PayMyBuddy.MoneyTransfer.mapper.ContactMapper;
import com.PayMyBuddy.MoneyTransfer.mapper.CreditCardMapper;
import com.PayMyBuddy.MoneyTransfer.mapper.UserMapper;
import com.PayMyBuddy.MoneyTransfer.model.Role;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.RoleRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger("MyUserDetailsService");
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private CreditCardMapper creditCardMapper;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public User findUser() {
        String userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userMail).orElse(null);
    }

    public UserDto findUserDto(){
        return userMapper.toDto(findUser());
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

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(UserRegistrationDto registrationDto, BindingResult result) {
        Optional<User> optionalUser = findByEmail(registrationDto.getEmail());

        if (optionalUser.isPresent()) {
            result.reject("user", "There is already an account registered with that email");
            logger.error("There is already an account registered with that email");
            return;
        }

        if (registrationDto.getEmail() == null || Objects.equals(registrationDto.getEmail(), "") ||
                (registrationDto.getPassword() == null || Objects.equals(registrationDto.getPassword(), ""))){
            result.reject("user", "Fields can't be empty");
            logger.error("Fields can't be empty");
            return;
        }

        if (!Objects.equals(registrationDto.getEmail(), registrationDto.getConfirmEmail())){
            result.reject("user", "The email addresses must match");
            logger.error("The email addresses must match");
            return;
        }

        if (!Objects.equals(registrationDto.getPassword(), registrationDto.getConfirmPassword())){
            result.reject("user", "The passwords must match");
            logger.error("The passwords must match");
            return;
        }

        User newUser = new User(registrationDto.getEmail(), passwordEncoder.encode(registrationDto.getPassword()));
        Set<Role> newUsersRole = new HashSet<>();
        newUsersRole.add(roleRepository.findByName("ROLE_USER"));
        newUser.setRoles(newUsersRole);
        newUser.setBalanceCurrencyCode(Currency.getInstance(Locale.getDefault()).toString());
        userRepository.save(newUser);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void addContact(ContactDto contactDto, BindingResult result) {
        User user = findUser();
        String contactEmail = contactDto.getEmail();

        if (Objects.equals(user.getEmail(), contactEmail)){
            result.reject("contact", "You can't add yourself to your contact list");
            logger.error("You can't add yourself to your contact list");
            return;
        }

        Optional<User> optionalContactUser = userRepository.findByEmail(contactEmail);
        if (optionalContactUser.isPresent()) {
            User newContact = optionalContactUser.get();
            Set<User> contacts = user.getContacts();
            if (contacts.contains(newContact)) {
                result.reject("contact",  "This person is already in the contact list");
                logger.error("This person is already in the contact list");
            } else {
                contacts.add(newContact);
                userRepository.save(user);
            }
        } else {
            result.reject("contact", "There is no registered user with this email");
            logger.error("There is no registered user with this email");
        }
    }

    public Iterable<ContactDto> getUserContacts() {
        ArrayList<ContactDto> result = new ArrayList<>();

        findUser().getContacts().forEach(
                contact -> result.add(contactMapper.toDto(contact))
        );

        return result;
    }
}
