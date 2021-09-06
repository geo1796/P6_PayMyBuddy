package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountDto;
import com.PayMyBuddy.MoneyTransfer.mapper.BankAccountMapper;
import com.PayMyBuddy.MoneyTransfer.model.BankAccount;
import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
@AllArgsConstructor
public class BankAccountService {

    private static final Logger logger = LogManager.getLogger("BankAccountService");
    private MyUserDetailsService myUserDetailsService;
    private BankAccountRepository bankAccountRepository;
    private BankAccountMapper bankAccountMapper;

    public ArrayList<BankAccountDto> findAllDtos() {
        ArrayList<BankAccountDto> result = new ArrayList<>();
        User user = myUserDetailsService.findUser();

        user.getBankAccounts().forEach(
                bankAccount -> result.add(bankAccountMapper.toDto(bankAccount))
        );

        return result;
    }

    public void addBankAccount(BankAccount newBankAccount, BindingResult result) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findByIban(newBankAccount.getIban());
        User user = myUserDetailsService.findUser();
        Set<BankAccount> usersBankAccounts = user.getBankAccounts();

        if(optionalBankAccount.isPresent()) {
            BankAccount alreadyInDbBankAccount = optionalBankAccount.get();

            if (usersBankAccounts.contains(alreadyInDbBankAccount)){
                logger.error("this credit card is already linked to this pay my buddy account");
                result.reject("creditCard");
            }
            else{
                usersBankAccounts.add(alreadyInDbBankAccount);
                myUserDetailsService.save(user);
            }
        }
        else{
            try{
                bankAccountRepository.save(newBankAccount);
                usersBankAccounts.add(newBankAccount);
                myUserDetailsService.save(user);
            }
            catch (DataIntegrityViolationException e){
                logger.error(e.getMessage());
                result.reject("bankAccount", Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    public Set<BankAccountDto> findBankAccountDtos() {
        HashSet<BankAccountDto> result = new HashSet<>();

        myUserDetailsService.findUser().getBankAccounts().forEach(
                bankAccount -> result.add(bankAccountMapper.toDto(bankAccount))
        );

        return result;
    }
}
