package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountDto;
import com.PayMyBuddy.MoneyTransfer.mapper.BankAccountMapper;
import com.PayMyBuddy.MoneyTransfer.model.BankAccount;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankAccountServiceTest {

    @Mock
    BankAccountMapper bankAccountMapper;
    @Mock
    MyUserDetailsService myUserDetailsService;
    @Mock
    BankAccountRepository bankAccountRepository;

    @InjectMocks
    BankAccountService bankAccountService;

    private User user;
    private BankAccount bankAccount;
    private BankAccountDto bankAccountDto;
    private HashSet<BankAccount> bankAccounts;

    @BeforeEach
    public void setup(){

        user = new User();
        bankAccount = new BankAccount();
        bankAccounts = new HashSet<>();
        bankAccountDto = new BankAccountDto();
        bankAccount.setIban("ibanTest");
        bankAccounts.add(bankAccount);
        bankAccountDto.setIban("ibanTest");
        user.setBankAccounts(bankAccounts);


    }

    @Test
    public void testFindBankAccountDtos(){
        HashSet<BankAccountDto> expected = new HashSet<>();
        expected.add(bankAccountDto);
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(bankAccountMapper.toDto(bankAccount)).thenReturn(bankAccountDto);
        assertEquals(expected, bankAccountService.findAllDtos());
    }

    @Test
    public void testAddBankAccountAlreadyLinked() {
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(bankAccountRepository.findByIban(bankAccount.getIban())).thenReturn(Optional.of(bankAccount));
        assertEquals(false, bankAccountService.addBankAccount(bankAccount, new BeanPropertyBindingResult(bankAccount, "bankAccount")));
    }

    @Test
    public void testAddBankAccountAlreadyInDb() {
        user.getBankAccounts().remove(bankAccount);

        when(myUserDetailsService.findUser()).thenReturn(user);
        when(bankAccountRepository.findByIban(bankAccount.getIban())).thenReturn(Optional.of(bankAccount));
        assertEquals(true, bankAccountService.addBankAccount(bankAccount, new BeanPropertyBindingResult(bankAccount, "bankAccount")));
    }

    @Test
    public void testAddBankAccountNotInDb() {
        when(myUserDetailsService.findUser()).thenReturn(user);
        when(bankAccountRepository.findByIban(bankAccount.getIban())).thenReturn(Optional.empty());
        assertEquals(true, bankAccountService.addBankAccount(bankAccount, new BeanPropertyBindingResult(bankAccount, "bankAccount")));
    }

}
