package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardDto;
import com.PayMyBuddy.MoneyTransfer.mapper.CreditCardMapper;
import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.CreditCardRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class CreditCardService {

    private static final Logger logger = LogManager.getLogger("CreditCardService");
    private CreditCardRepository creditCardRepository;
    private MyUserDetailsService myUserDetailsService;
    private CreditCardMapper creditCardMapper;

    public void addCreditCard(CreditCardDto creditCardDto, BindingResult result) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(dateFormat.parse(creditCardDto.getExpirationDate()).before(Date.from(Instant.now()))) { //checking if the credit card is expired
                result.reject("creditCard", "credit card expired");
                logger.error("credit card expired");
                return;
            }
        } catch (ParseException e) {
            result.reject("creditCard", "can't parse expiration date");
            logger.error("can't parse expiration date");
            return;
        }

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findByCardNumber(creditCardDto.getCardNumber());
        User user = myUserDetailsService.findUser();
        Set<CreditCard> usersCreditCards = user.getCreditCards();

        if(optionalCreditCard.isPresent()) {
            logger.info("this credit card is already in db");
            CreditCard alreadyInDbCreditCard = optionalCreditCard.get();

            if (usersCreditCards.contains(alreadyInDbCreditCard)){ //checking if this creditCard is already linked to this account
                logger.error("this credit card is already linked to this pay my buddy account");
                result.reject("creditCard", "this credit card is already linked to this pay my buddy account");
            }
            else{
                usersCreditCards.add(alreadyInDbCreditCard);
                myUserDetailsService.save(user);
                logger.info("credit card successfully added");
            }
        }
        else{
            try{
                CreditCard newCreditCard = creditCardMapper.toEntity(creditCardDto);
                creditCardRepository.save(newCreditCard);
                usersCreditCards.add(newCreditCard);
                myUserDetailsService.save(user);
                logger.info("credit card successfully added");
            }
            catch (DataIntegrityViolationException e){
                logger.error(e.getMessage());
                result.reject("creditCard", "an error occurred");
            }
        }
    }

    public Set<CreditCard> getAllUsersCreditCards() {
        return myUserDetailsService.findUser().getCreditCards();
    }

    public Set<CreditCardDto> findCreditCardDtos() {
        HashSet<CreditCardDto> result = new HashSet<>();

        myUserDetailsService.findUser().getCreditCards().forEach(
                creditCard -> result.add(creditCardMapper.toDto(creditCard))
        );

        return result;
    }
}
