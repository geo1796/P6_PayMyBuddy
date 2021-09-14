package com.PayMyBuddy.MoneyTransfer.mapper;

import com.PayMyBuddy.MoneyTransfer.dto.ContactDto;
import com.PayMyBuddy.MoneyTransfer.dto.TransactionDto;
import com.PayMyBuddy.MoneyTransfer.dto.UserDto;
import com.PayMyBuddy.MoneyTransfer.model.User;
import com.PayMyBuddy.MoneyTransfer.util.Currency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserMapper {

    private ContactMapper contactMapper;
    private TransactionMapper transactionMapper;

    public UserDto toDto(User user) {
        if(user == null)
            return null;

        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setBalance(user.getBalance());
        dto.setBalanceCurrencyCode(Currency.valueOf(user.getBalanceCurrencyCode()).symbol); //to display the symbol of the currency

        List<ContactDto> contactDtoList = new ArrayList<>();
        user.getContacts().forEach(
                contact -> contactDtoList.add(contactMapper.toDto(contact))
        );
        dto.setContacts(contactDtoList);

        List<TransactionDto> transactionsAsSenderDtos = new ArrayList<>();
        user.getTransactionsAsReceiver().forEach(
                transaction -> transactionsAsSenderDtos.add(transactionMapper.toDto(transaction))
        );
        dto.setTransactionsAsSender(transactionsAsSenderDtos);

        List<TransactionDto> transactionsAsReceiverDtos = new ArrayList<>();
        user.getTransactionsAsReceiver().forEach(
                transaction -> transactionsAsReceiverDtos.add(transactionMapper.toDto(transaction))
        );
        dto.setTransactionsAsSender(transactionsAsSenderDtos);

        return dto;
    }
}
