package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardTransactionDto;
import com.PayMyBuddy.MoneyTransfer.mapper.CreditCardTransactionMapper;
import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import com.PayMyBuddy.MoneyTransfer.model.CreditCardTransaction;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.CreditCardRepository;
import com.PayMyBuddy.MoneyTransfer.repository.CreditCardTransactionRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreditCardTransactionService {

    private CreditCardTransactionRepository creditCardTransactionRepository;
    private CreditCardTransactionMapper creditCardTransactionMapper;
    private MyUserDetailsService myUserDetailsService;
    private UserRepository userRepository;
    private CreditCardRepository creditCardRepository;
    private static final Logger logger = LogManager.getLogger("CreditCardTransactionService");

    public List<CreditCardTransactionDto> getCreditCardTransactionDtos() {
        ArrayList<CreditCardTransactionDto> result = new ArrayList<>();

        myUserDetailsService.findUser().getCreditCardTransactions().forEach(
                creditCardTransaction -> result.add(creditCardTransactionMapper.toDto(creditCardTransaction))
        );

        return result;
    }

    public void addCreditCardTransaction(CreditCardTransactionDto creditCardTransactionDto, BindingResult result) {


        //TODO taux de change

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findByCardNumber(creditCardTransactionDto.getCardNumber());
        if(optionalCreditCard.isPresent()) {
            CreditCard creditCard = optionalCreditCard.get();

            if(creditCard.getExpirationDate().after(Date.from(Instant.now()))) { //checking if this creditCard is expired
                User user = myUserDetailsService.findUser();
                CreditCardTransaction creditCardTransaction = creditCardTransactionMapper.toEntity(creditCardTransactionDto, user, creditCard);
                creditCardTransactionRepository.save(creditCardTransaction);
                user.setBalance(user.getBalance() + creditCardTransactionDto.getAmount());
                userRepository.save(user);
            }
            else
                result.reject("creditCardTransaction", "this credit card is expired");
        }
        else
            result.reject("creditCardTransaction", "unknown credit card number");


    }


}
