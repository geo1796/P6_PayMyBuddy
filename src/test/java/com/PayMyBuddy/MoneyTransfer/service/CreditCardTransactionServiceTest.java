package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardDto;
import com.PayMyBuddy.MoneyTransfer.dto.CreditCardTransactionDto;
import com.PayMyBuddy.MoneyTransfer.mapper.BankAccountMapper;
import com.PayMyBuddy.MoneyTransfer.mapper.CreditCardMapper;
import com.PayMyBuddy.MoneyTransfer.mapper.CreditCardTransactionMapper;
import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import com.PayMyBuddy.MoneyTransfer.model.CreditCardTransaction;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.CreditCardRepository;
import com.PayMyBuddy.MoneyTransfer.repository.CreditCardTransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    private User user;
    private CreditCardTransaction creditCardTransaction;


    @BeforeAll
    public void setup(){
        creditCardTransaction = new CreditCardTransaction();
        creditCardTransaction.setAmount(10.);
        creditCardTransaction.setCreditCard(creditCard);
        creditCardTransaction.setCurrencyCode("EUR");

        user = new User();
        user.setBalanceCurrencyCode("EUR");
    }

    @BeforeEach
    public void eachSetup(){
        result = new BeanPropertyBindingResult(creditCardTransactionDto, "creditCardTransaction");

        Set<CreditCardTransaction> usersCreditCardTransactions = new HashSet<>();
        usersCreditCardTransactions.add(creditCardTransaction);
        user.setCreditCardTransactions(usersCreditCardTransactions);
        HashSet<CreditCard> usersCreditCards = new HashSet<>();
        usersCreditCards.add(creditCard);
        user.setCreditCards(usersCreditCards);

        creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setAmount(10.);
        creditCardTransactionDto.setCardNumber("123");
        creditCardTransactionDto.setCurrencyCode("EUR");
        creditCard = new CreditCard();
        creditCard.setCardNumber(creditCardTransactionDto.getCardNumber());
        creditCard.setExpirationDate(Date.valueOf("2025-01-01"));
        creditCard.setCardNumber("123");
    }

    @Test
    public void testAddCreditCardTransactionWithoutCreditCard(){
        when(myUserDetailsService.findUser()).thenReturn(user);
        user.setCreditCards(new HashSet<>());
        creditCardTransactionService.addCreditCardTransaction(creditCardTransactionDto, result);
        assertTrue(result.hasErrors());
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
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(creditCardRepository.findByCardNumber(creditCardTransactionDto.getCardNumber())).thenReturn(Optional.ofNullable(creditCard));
        creditCardTransactionService.addCreditCardTransaction(creditCardTransactionDto, result);
        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddCreditCardTransactionSuccess() {
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(creditCardRepository.findByCardNumber(creditCardTransactionDto.getCardNumber())).thenReturn(Optional.ofNullable(creditCard));
        creditCardTransactionService.addCreditCardTransaction(creditCardTransactionDto, result);
        assertFalse(result.hasErrors());
    }

    @Test
    public void testGetCreditCardTransactionDtos() {
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(creditCardTransactionMapper.toDto(creditCardTransaction)).thenReturn(creditCardTransactionDto);

        List<CreditCardTransactionDto> actual = creditCardTransactionService.getCreditCardTransactionDtos();
        List<CreditCardTransactionDto> expected = new ArrayList<>();
        expected.add(creditCardTransactionDto);
        assertEquals(expected, actual);
    }

}
