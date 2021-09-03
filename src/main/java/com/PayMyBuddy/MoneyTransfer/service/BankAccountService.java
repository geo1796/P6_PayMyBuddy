package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountDto;
import com.PayMyBuddy.MoneyTransfer.mapper.BankAccountMapper;
import com.PayMyBuddy.MoneyTransfer.model.BankAccount;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        User user = myUserDetailsService.findUser();
        boolean alreadyAdded = false;
        int i = 0;
        List<BankAccount> usersBankAccounts = user.getBankAccounts();
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        bankAccounts.removeAll(usersBankAccounts);

        while (i < usersBankAccounts.size() && !alreadyAdded) {
            BankAccount bankAccount = usersBankAccounts.get(i);
            i++;
            if (Objects.equals(bankAccount.getIban(), newBankAccount.getIban())) {
                logger.error("This account has been added already");
                result.rejectValue("iban", "This account has been added already");
                alreadyAdded = true;
            }
        }

        i = 0;
        while (i < bankAccounts.size() && !alreadyAdded) {
            BankAccount bankAccount = bankAccounts.get(i);
            i++;
            if (Objects.equals(bankAccount.getIban(), newBankAccount.getIban())) {
                logger.info("this bank account is already saved in db");
                usersBankAccounts.add(newBankAccount);
                logger.info("bank account successfully added to user");
                myUserDetailsService.save(user);
                alreadyAdded = true;
            }

            if (!alreadyAdded) {
                try {
                    bankAccountRepository.save(newBankAccount);
                    usersBankAccounts.add(newBankAccount);
                    myUserDetailsService.save(user);
                } catch (DataIntegrityViolationException e) {
                    logger.error(e.getMessage());
                    result.rejectValue("iban", Objects.requireNonNull(e.getMessage()));
                }
            }
        }
    }
}
