package com.PayMyBuddy.MoneyTransfer.controller;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class TransactionController {

    @ModelAttribute("receiver")
    public ContactDto senderDto(){ return new ContactDto(); }

}
