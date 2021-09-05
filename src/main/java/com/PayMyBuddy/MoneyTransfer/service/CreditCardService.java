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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreditCardService {

    private static final Logger logger = LogManager.getLogger("CreditCardService");
    private CreditCardRepository creditCardRepository;
    private MyUserDetailsService myUserDetailsService;
    private CreditCardMapper creditCardMapper;

    public void addCreditCard(CreditCardDto creditCardDto, BindingResult result) {
        Optional<CreditCard> optionalCreditCard = creditCardRepository.findByCardNumber(creditCardDto.getCardNumber());
        User user = myUserDetailsService.findUser();
        List<CreditCard> usersCreditCards = user.getCreditCards();

        if(optionalCreditCard.isPresent()) {
            CreditCard alreadyInDbCreditCard = optionalCreditCard.get();

            if (usersCreditCards.contains(alreadyInDbCreditCard)){
                logger.error("this credit card is already linked to this pay my buddy account");
                result.reject("creditCard");
            }
            else{
                usersCreditCards.add(alreadyInDbCreditCard);
                myUserDetailsService.save(user);
            }
        }
        else{
            try{
                CreditCard newCreditCard = creditCardMapper.toEntity(creditCardDto);
                creditCardRepository.save(newCreditCard);
                usersCreditCards.add(newCreditCard);
                myUserDetailsService.save(user);
            }
            catch (DataIntegrityViolationException e){
                logger.error(e.getMessage());
                result.reject("crediCard", Objects.requireNonNull(e.getMessage()));
            }
        }
    }
}
