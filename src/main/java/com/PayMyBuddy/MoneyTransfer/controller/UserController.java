package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@AllArgsConstructor
public class UserController {

    private MyUserDetailsService myUserDetailsService;
    private static final Logger logger = LogManager.getLogger("UserController");

    @ModelAttribute("contact")
    public ContactDto contactDto() {
        return new ContactDto();
    }

    @GetMapping("addContact")
    public String showNewContactForm(){
        return "new-contact-form";
    }

    @PostMapping("/addContact")
    public String addContact(@ModelAttribute("contact") @Valid ContactDto contactDto,
                             BindingResult result){
        logger.info("calling method : addContact");

        myUserDetailsService.addContact(contactDto, result);
        logger.info("contact added : " +contactDto.getEmail());

        if (result.hasErrors()){
            logger.error("can't add contact : ");
            return "redirect:/addContact?error";
        }

        return "redirect:/addContact?success";
    }

    @GetMapping("/contacts")
    public String showContactList(Model model){
        model.addAttribute("contactList", myUserDetailsService.getUserContacts());
        return "contact-list";
    }

}
