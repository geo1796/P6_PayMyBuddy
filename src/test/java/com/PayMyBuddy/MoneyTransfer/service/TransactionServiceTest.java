package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.TransactionRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionServiceTest {

    @Mock
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionDto transactionDto;
    private BindingResult result;
    private User sender;
    private User receiver;

    @BeforeAll
    public void setup(){
        transactionDto = new TransactionDto();
        sender = new User();
        sender.setBalanceCurrencyCode("EUR");
        receiver = new User();
        receiver.setEmail("receiver@mail.com");
        receiver.setBalanceCurrencyCode("EUR");
        transactionDto.setReceiverEmail(receiver.getEmail());
        transactionDto.setCurrencyCode("EUR");
    }

    @BeforeEach()
    public void eachSetup() {
        sender.setBalance(15.);
        transactionDto.setAmount(10.);
        result = new BeanPropertyBindingResult(transactionDto, "transaction");
    }

    @Test
    public void testAddTransactionWithNullAmount(){
        transactionDto.setAmount(0.);
        transactionService.addTransaction(transactionDto, result);

        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddTransactionWithoutReceiver(){
        when(myUserDetailsService.findByEmail(receiver.getEmail())).thenReturn(Optional.empty());
        transactionService.addTransaction(transactionDto, result);
        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddTransactionWithoutEnoughMoney(){
        sender.setBalance(0.);
        when(myUserDetailsService.findUser()).thenReturn(sender);
        when(myUserDetailsService.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        transactionService.addTransaction(transactionDto, result);

        assertTrue(result.hasErrors());
    }

    @Test
    public void testAddTransactionSuccess(){
        when(myUserDetailsService.findUser()).thenReturn(sender);
        when(myUserDetailsService.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        transactionService.addTransaction(transactionDto, result);

        assertFalse(result.hasErrors());
    }
}
