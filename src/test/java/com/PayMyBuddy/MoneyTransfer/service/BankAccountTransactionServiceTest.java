package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountTransactionDto;
import com.PayMyBuddy.MoneyTransfer.mapper.BankAccountTransactionMapper;
import com.PayMyBuddy.MoneyTransfer.model.BankAccount;
import com.PayMyBuddy.MoneyTransfer.model.BankAccountTransaction;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.BankAccountRepository;
import com.PayMyBuddy.MoneyTransfer.repository.BankAccountTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountTransactionServiceTest {

    @Mock
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private BankAccountTransactionMapper bankAccountTransactionMapper;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private BankAccountTransactionRepository bankAccountTransactionRepository;

    @InjectMocks
    private BankAccountTransactionService bankAccountTransactionService;

    private User user;
    private BankAccountTransaction bankAccountTransaction;
    private BankAccountTransactionDto bankAccountTransactionDto;

    @BeforeEach
    public void setup() {
        user = new User();
        HashSet<BankAccountTransaction> bankAccountTransactions = new HashSet<>();
        bankAccountTransaction = new BankAccountTransaction();
        bankAccountTransactions.add(bankAccountTransaction);
        user.setBankAccountTransactions(bankAccountTransactions);
        bankAccountTransactionDto = new BankAccountTransactionDto(10, "EUR", "ibanTest", true);
    }

    @Test
    public void testFindBankAccountTransactionDtos() {
        HashSet<BankAccountTransactionDto> expected = new HashSet<>();
        expected.add(bankAccountTransactionDto);

        when(bankAccountTransactionMapper.toDto(bankAccountTransaction)).thenReturn(bankAccountTransactionDto);
        when(myUserDetailsService.findUser()).thenReturn(user);
        assertEquals(expected, bankAccountTransactionService.findBankAccountTransactionDtos());
    }

    @Test
    public void testSendMoneyToBankAccountWithoutEnoughMoney() {
        BankAccount bankAccount = new BankAccount();

        bankAccountTransactionDto.setAmount(10000000.);
        bankAccountTransactionDto.setToBalance(false);
        user.setBalanceCurrencyCode("EUR");
        user.setBalance(0.);
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(bankAccountRepository.findByIban(anyString())).thenReturn(Optional.of(bankAccount));

        BindingResult result = new BeanPropertyBindingResult(bankAccountTransactionDto, "bankAccount");
        bankAccountTransactionService.addBankAccountTransaction(bankAccountTransactionDto, result);
        assertTrue(result.hasErrors());
    }

    @Test
    public void testSendMoneyToBankAccountWithEnoughMoney() {
        BankAccount bankAccount = new BankAccount();

        user.setBalance(20.);
        user.setBalanceCurrencyCode("EUR");
        bankAccountTransactionDto.setToBalance(false);
        when(bankAccountTransactionMapper.toEntity(bankAccountTransactionDto, user, bankAccount)).thenReturn(bankAccountTransaction);
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(bankAccountRepository.findByIban(anyString())).thenReturn(Optional.of(bankAccount));


        BindingResult result = new BeanPropertyBindingResult(bankAccountTransactionDto, "bankAccount");
        bankAccountTransactionService.addBankAccountTransaction(bankAccountTransactionDto, result);
        assertFalse(result.hasErrors());
    }

    @Test
    public void testSendMoneyToPayMyBuddyAccount() {
        BankAccount bankAccount = new BankAccount();

        bankAccountTransactionDto.setAmount(10000000.);
        user.setBalanceCurrencyCode("EUR");
        when(bankAccountTransactionMapper.toEntity(bankAccountTransactionDto, user, bankAccount)).thenReturn(bankAccountTransaction);
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(bankAccountRepository.findByIban(anyString())).thenReturn(Optional.of(bankAccount));


        BindingResult result = new BeanPropertyBindingResult(bankAccountTransactionDto, "bankAccount");
        bankAccountTransactionService.addBankAccountTransaction(bankAccountTransactionDto, result);
        assertFalse(result.hasErrors());
    }

}
