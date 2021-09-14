package com.PayMyBuddy.MoneyTransfer.mapper;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountTransactionDto;
import com.PayMyBuddy.MoneyTransfer.model.BankAccount;
import com.PayMyBuddy.MoneyTransfer.model.BankAccountTransaction;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.util.Currency;
import org.springframework.stereotype.Component;

@Component
public class BankAccountTransactionMapper {

    public BankAccountTransactionDto toDto(BankAccountTransaction bankAccountTransaction){
        BankAccountTransactionDto result = new BankAccountTransactionDto();

        result.setAmount(bankAccountTransaction.getAmount());
        result.setCurrencyCode(Currency.valueOf(bankAccountTransaction.getCurrencyCode()).symbol); //to display the symbol of the currency 
        result.setIban(bankAccountTransaction.getBankAccount().getIban());
        result.setToBalance(bankAccountTransaction.getToBalance());

        return result;
    }

    public BankAccountTransaction toEntity(BankAccountTransactionDto bankAccountTransactionDto, User user, BankAccount bankAccount) {
        BankAccountTransaction bankAccountTransaction = new BankAccountTransaction();

        bankAccountTransaction.setBankAccount(bankAccount);
        bankAccountTransaction.setAmount(bankAccountTransactionDto.getAmount());
        bankAccountTransaction.setCurrencyCode(bankAccountTransactionDto.getCurrencyCode());
        bankAccountTransaction.setUser(user);
        bankAccountTransaction.setToBalance(bankAccountTransactionDto.getToBalance());

        return bankAccountTransaction;
    }
}
