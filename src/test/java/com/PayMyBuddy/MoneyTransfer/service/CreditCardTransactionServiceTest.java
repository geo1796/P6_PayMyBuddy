package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardTransactionDto;
import com.PayMyBuddy.MoneyTransfer.mapper.CreditCardTransactionMapper;
import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.CreditCardRepository;
import com.PayMyBuddy.MoneyTransfer.repository.CreditCardTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditCardTransactionServiceTest {

    @Mock
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private CreditCardTransactionMapper creditCardTransactionMapper;
    @Mock
    private CreditCardTransactionRepository creditCardTransactionRepository;

    @InjectMocks
    private CreditCardTransactionService creditCardTransactionService;

    private CreditCardTransactionDto creditCardTransactionDto;
    private CreditCard creditCard;
    private BindingResult result;


    @BeforeEach
    public void setup(){
        result = new BeanPropertyBindingResult(creditCardTransactionDto, "creditCardTransaction");
        creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setAmount(10.);
        creditCardTransactionDto.setCardNumber("123");
        creditCardTransactionDto.setCurrencyCode("EUR");
        creditCard = new CreditCard();
        creditCard.setCardNumber(creditCardTransactionDto.getCardNumber());
        creditCard.setExpirationDate(Date.valueOf("2025-01-01"));
    }

    @Test
    public void testAddCreditCardTransactionWithNullAmount(){
        creditCardTransactionDto.setAmount(0.);
        creditCardTransactionService.addCreditCardTransaction(creditCardTransactionDto, result);
        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddCreditCardTransactionWithExpiredCreditCard() {
        creditCard.setExpirationDate(Date.valueOf("2000-01-01"));
        when(creditCardRepository.findByCardNumber(creditCardTransactionDto.getCardNumber())).thenReturn(Optional.ofNullable(creditCard));
        creditCardTransactionService.addCreditCardTransaction(creditCardTransactionDto, result);
        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddCreditCardTransactionSuccess() {
        User user = new User();
        HashSet<CreditCard> usersCreditCards = new HashSet<>();
        usersCreditCards.add(creditCard);
        user.setCreditCards(usersCreditCards);
        user.setBalanceCurrencyCode("EUR");

        when(myUserDetailsService.findUser()).thenReturn(user);
        when(creditCardRepository.findByCardNumber(creditCardTransactionDto.getCardNumber())).thenReturn(Optional.ofNullable(creditCard));
        creditCardTransactionService.addCreditCardTransaction(creditCardTransactionDto, result);
        assertFalse(result.hasErrors());
    }

}
