package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import com.PayMyBuddy.MoneyTransfer.dto.UserDto;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    private MyUserDetailsService myUserDetailsService;

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") final int id) {
        Optional<UserDto> optionalUser = myUserDetailsService.getUserDto(id);

        if(optionalUser.isPresent()){
            UserDto userFound = optionalUser.get();
            return new ResponseEntity<>(userFound, HttpStatus.OK);
        }

        return new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<Iterable<UserDto>> getAllUsers(){
        return new ResponseEntity<>(myUserDetailsService.getAllUserDtos(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}/contacts/{email}")
    public ResponseEntity<ContactDto> getContact(@PathVariable("id") final int id, @PathVariable("email") final String contactEmail){
        Optional<UserDto> optionalUserDto = myUserDetailsService.findUserDtoById(id);
        if (optionalUserDto.isPresent()) {
            UserDto userDto = optionalUserDto.get();
            Optional<ContactDto> optionalContactDto = myUserDetailsService.findUserContact(userDto, contactEmail);
            if (optionalContactDto.isPresent())
                return new ResponseEntity<>(optionalContactDto.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ContactDto(), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/{id}/contacts/{email}")
    public ResponseEntity<String> addContact(@PathVariable("id") final int id, @PathVariable("email") final String contactEmail){
        if(myUserDetailsService.addContact(id, contactEmail))
            return new ResponseEntity<>(contactEmail, HttpStatus.OK);

        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }

}
