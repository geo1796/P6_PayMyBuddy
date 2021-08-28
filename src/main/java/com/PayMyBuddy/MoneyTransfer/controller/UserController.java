package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import com.PayMyBuddy.MoneyTransfer.dto.UserDto;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.PayMyBuddy.MoneyTransfer.util.Json.toJson;

@RestController
@AllArgsConstructor
public class UserController {

    private MyUserDetailsService myUserDetailsService;
    private static final Logger logger = LogManager.getLogger("UserController");

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") final int id) {
        logger.info("calling method getUser");
        Optional<UserDto> optionalUser = myUserDetailsService.getUserDto(id);

        if(optionalUser.isPresent()){
            UserDto userFound = optionalUser.get();
            return new ResponseEntity<>(userFound, HttpStatus.OK);
        }

        logger.error("no user with id : " + id);
        return new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<Iterable<UserDto>> getAllUsers(){
        logger.info("calling method : getAllUsers");
        return new ResponseEntity<>(myUserDetailsService.getAllUserDtos(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}/contacts/{email}")
    public ResponseEntity<ContactDto> getContact(@PathVariable("id") final int id, @PathVariable("email") final String contactEmail){
        logger.info("calling method : getContact / user id : " + id + " / contact email : " + contactEmail);
        Optional<UserDto> optionalUserDto = myUserDetailsService.findUserDtoById(id);
        if (optionalUserDto.isPresent()) {
            UserDto userDto = optionalUserDto.get();
            Optional<ContactDto> optionalContactDto = myUserDetailsService.findUserContact(userDto, contactEmail);
            if (optionalContactDto.isPresent())
                return new ResponseEntity<>(optionalContactDto.get(), HttpStatus.OK);
            else
                logger.error("contact with email : " + contactEmail);
        }
        else
            logger.error("no user with id : " + id);
        return new ResponseEntity<>(new ContactDto(), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/{id}/contacts")
    public ResponseEntity<ContactDto> addContact(@PathVariable("id") final int id, @RequestBody ContactDto contactDto){
        logger.info("calling method : addContact / user id : " + id + " / body : " + toJson(contactDto));
        try {
            myUserDetailsService.addContact(id, contactDto);
            return new ResponseEntity<>(contactDto, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            logger.error("can't add contact : " + e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
