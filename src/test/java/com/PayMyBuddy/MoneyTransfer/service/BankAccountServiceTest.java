package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountDto;
import com.PayMyBuddy.MoneyTransfer.mapper.BankAccountMapper;
import com.PayMyBuddy.MoneyTransfer.model.BankAccount;
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
public class BankAccountServiceTest {

    @Mock
    BankAccountMapper bankAccountMapper;
    @Mock
    MyUserDetailsService myUserDetailsService;

    @InjectMocks
    BankAccountService bankAccountService;

    @Test
    public void testFindBankAccountDtos(){
        //setting up
        HashSet<BankAccountDto> expected = new HashSet<>();
        User user = new User();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban("ibanTest");
        HashSet<BankAccount> bankAccounts = new HashSet<>();
        bankAccounts.add(bankAccount);
        user.setBankAccounts(bankAccounts);
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setIban("ibanTest");
        expected.add(bankAccountDto);
        //set up done

        when(myUserDetailsService.findUser()).thenReturn(user);
        when(bankAccountMapper.toDto(bankAccount)).thenReturn(bankAccountDto);
        assertEquals(expected, bankAccountService.findBankAccountDtos());
    }



}
