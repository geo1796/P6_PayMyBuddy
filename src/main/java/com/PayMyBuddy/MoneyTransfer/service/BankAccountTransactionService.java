package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountTransactionDto;
import com.PayMyBuddy.MoneyTransfer.mapper.BankAccountTransactionMapper;
import com.PayMyBuddy.MoneyTransfer.mapper.CreditCardMapper;
import com.PayMyBuddy.MoneyTransfer.model.*;
import com.PayMyBuddy.MoneyTransfer.repository.BankAccountRepository;
import com.PayMyBuddy.MoneyTransfer.repository.BankAccountTransactionRepository;
import com.PayMyBuddy.MoneyTransfer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class BankAccountTransactionService {

    private static final Logger logger = LogManager.getLogger("BankAccountTransactionService");
    private BankAccountRepository bankAccountRepository;
    private BankAccountTransactionRepository bankAccountTransactionRepository;
    private MyUserDetailsService myUserDetailsService;
    private BankAccountTransactionMapper bankAccountTransactionMapper;
    private UserRepository userRepository;

    public List<BankAccountTransaction> findAll() {
        return bankAccountTransactionRepository.findAll();
    }

    public Set<BankAccountTransactionDto> findBankAccountTransactionDtos() {
        HashSet<BankAccountTransactionDto> result = new HashSet<>();

        myUserDetailsService.findUser().getBankAccountTransactions().forEach(
                bankAccountTransaction -> result.add(bankAccountTransactionMapper.toDto(bankAccountTransaction))
        );

        return result;
    }

    public void addBankAccountTransaction(BankAccountTransactionDto bankAccountTransactionDto, BindingResult result) {

        //TODO taux de change

        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findByIban(bankAccountTransactionDto.getIban());
        if(optionalBankAccount.isPresent()) {
            BankAccount bankAccount = optionalBankAccount.get();

                User user = myUserDetailsService.findUser();
                BankAccountTransaction bankAccountTransaction = bankAccountTransactionMapper.toEntity(bankAccountTransactionDto, user, bankAccount);
                bankAccountTransactionRepository.save(bankAccountTransaction);

                double transactionAmount = bankAccountTransaction.getAmount();
                if (bankAccountTransaction.getToBalance())
                    user.setBalance(user.getBalance() + transactionAmount);
                else{
                    double usersBalance = user.getBalance();
                    if (usersBalance > transactionAmount)
                        user.setBalance(user.getBalance() - bankAccountTransaction.getAmount());
                    else{
                        result.reject("bankAccountTransaction", "not enough money on this Pay My Buddy account to proceed transaction");
                        logger.error("not enough money on this Pay My Buddy account to proceed transaction");
                        return;
                    }
                }

                userRepository.save(user);

        }
        else {
            result.reject("bankAccountTransaction", "unknown iban");
            logger.error("unknown iban");
        }


    }
}
