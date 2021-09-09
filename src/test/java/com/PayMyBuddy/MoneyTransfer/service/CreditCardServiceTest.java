package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.CreditCardDto;
import com.PayMyBuddy.MoneyTransfer.mapper.CreditCardMapper;
import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.CreditCardRepository;
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

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditCardServiceTest {

    @Mock
    CreditCardMapper creditCardMapper;
    @Mock
    CreditCardRepository creditCardRepository;
    @Mock
    MyUserDetailsService myUserDetailsService;

    @InjectMocks
    CreditCardService creditCardService;

    private User user;
    private CreditCard creditCard;
    private CreditCardDto creditCardDto;

    @BeforeAll
    public void setup(){
        user = new User();
        creditCard = new CreditCard();
        creditCardDto = new CreditCardDto();

        creditCard.setCardNumber("123");
        creditCardDto.setCardNumber(creditCard.getCardNumber());
    }

    @BeforeEach
    public void eachSetup(){
        creditCardDto.setExpirationDate("2025-01-01");
        HashSet<CreditCard> usersCreditCards = new HashSet<>();
        usersCreditCards.add(creditCard);
        user.setCreditCards(usersCreditCards);
    }

    @Test
    public void testAddCreditCardNotInDb() {
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(creditCardMapper.toEntity(creditCardDto)).thenReturn(creditCard);
        when(creditCardRepository.findByCardNumber(creditCardDto.getCardNumber())).thenReturn(Optional.empty());

        BindingResult result = new BeanPropertyBindingResult(creditCardDto, "creditCard");
        creditCardService.addCreditCard(creditCardDto, result);
        assertFalse(result.hasErrors());
    }

    @Test
    public void testAddCreditCardAlreadyInDb() {
        user.setCreditCards(new HashSet<>());
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(creditCardRepository.findByCardNumber(creditCardDto.getCardNumber())).thenReturn(Optional.of(creditCard));

        BindingResult result = new BeanPropertyBindingResult(creditCardDto, "creditCard");
        creditCardService.addCreditCard(creditCardDto, result);
        assertFalse(result.hasErrors());
    }

    @Test
    public void testAddAlreadyAddedCreditCard() {
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(creditCardRepository.findByCardNumber(creditCardDto.getCardNumber())).thenReturn(Optional.of(creditCard));

        BindingResult result = new BeanPropertyBindingResult(creditCardDto, "creditCard");
        creditCardService.addCreditCard(creditCardDto, result);
        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddExpiredCreditCard() {
        creditCardDto.setExpirationDate("2000-01-01");

        BindingResult result = new BeanPropertyBindingResult(creditCardDto, "creditCard");
        creditCardService.addCreditCard(creditCardDto, result);
        assertTrue(result.hasErrors());
    }

}
