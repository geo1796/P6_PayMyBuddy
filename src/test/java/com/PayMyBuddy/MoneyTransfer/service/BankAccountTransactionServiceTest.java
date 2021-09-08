package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountTransactionDto;
import com.PayMyBuddy.MoneyTransfer.mapper.BankAccountTransactionMapper;
import com.PayMyBuddy.MoneyTransfer.model.BankAccountTransaction;
import com.PayMyBuddy.MoneyTransfer.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountTransactionServiceTest {

    @Mock
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private BankAccountTransactionMapper bankAccountTransactionMapper;

    @InjectMocks
    private BankAccountTransactionService bankAccountTransactionService;

    @Test
    public void testFindBankAccountTransactionDtos() {
        HashSet<BankAccountTransactionDto> expected = new HashSet<>();

        // setting up
        User user = new User();
        HashSet<BankAccountTransaction> bankAccountTransactions = new HashSet<>();
        BankAccountTransaction bankAccountTransaction = new BankAccountTransaction();
        bankAccountTransactions.add(bankAccountTransaction);
        user.setBankAccountTransactions(bankAccountTransactions);

        HashSet<BankAccountTransactionDto> bankAccountTransactionDtos = new HashSet<>();
        BankAccountTransactionDto bankAccountTransactionDto = new BankAccountTransactionDto(10, "EUR", "ibanTest", true);

        expected.add(bankAccountTransactionDto);
        // set up done

        when(bankAccountTransactionMapper.toDto(bankAccountTransaction)).thenReturn(bankAccountTransactionDto);
        when(myUserDetailsService.findUser()).thenReturn(user);
        assertEquals(expected, bankAccountTransactionService.findBankAccountTransactionDtos());
    }

}
