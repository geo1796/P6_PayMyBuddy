package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.mapper.TransactionMapper;
import com.PayMyBuddy.MoneyTransfer.model.Transaction;
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

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionServiceTest {

    @Mock
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;

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

        HashSet<User> contacts = new HashSet<>();
        contacts.add(receiver);
        sender.setContacts(contacts);
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
        when(myUserDetailsService.findUser()).thenReturn(sender);
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

    @Test
    public void testGetTransactionsAsSenderDtos(){
        Set<Transaction> transactionsAsSender = new HashSet<>();
        Transaction transactionAsSender = new Transaction();
        transactionsAsSender.add(transactionAsSender);
        sender.setTransactionsAsSender(transactionsAsSender);

        when(myUserDetailsService.findUser()).thenReturn(sender);
        when(transactionMapper.toDto(transactionAsSender)).thenReturn(transactionDto);

        List<TransactionDto> expected = new ArrayList<>();
        expected.add(transactionDto);
        List<TransactionDto> actual = transactionService.getTransactionAsSenderDtos();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTransactionsAsReceiverDtos() {
        Set<Transaction> transactionsAsReceiver = new HashSet<>();
        Transaction transactionAsReceiver = new Transaction();
        transactionsAsReceiver.add(transactionAsReceiver);
        sender.setTransactionsAsReceiver(transactionsAsReceiver);

        when(myUserDetailsService.findUser()).thenReturn(sender);
        when(transactionMapper.toDto(transactionAsReceiver)).thenReturn(transactionDto);

        List<TransactionDto> expected = new ArrayList<>();
        expected.add(transactionDto);
        List<TransactionDto> actual = transactionService.getTransactionAsReceiverDtos();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllTransactionDtos(){
        testGetTransactionsAsReceiverDtos();
        testGetTransactionsAsSenderDtos();

        List<TransactionDto> expected = new ArrayList<>();
        transactionDto.setEndDate(Date.from(Instant.now()));
        expected.add(transactionDto);
        expected.add(transactionDto);

        List<TransactionDto> actual = transactionService.getAllTransactionDtos();

        assertEquals(expected, actual);
    }
}
