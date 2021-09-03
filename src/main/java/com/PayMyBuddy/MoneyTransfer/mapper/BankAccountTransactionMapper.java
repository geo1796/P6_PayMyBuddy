package com.PayMyBuddy.MoneyTransfer.mapper;

import com.PayMyBuddy.MoneyTransfer.dto.BankAccountTransactionDto;
import com.PayMyBuddy.MoneyTransfer.model.BankAccountTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountTransactionMapper {
    BankAccountTransactionMapper INSTANCE = Mappers.getMapper(BankAccountTransactionMapper.class);

    BankAccountTransactionDto toDto(BankAccountTransaction bankAccountTransaction);
}
