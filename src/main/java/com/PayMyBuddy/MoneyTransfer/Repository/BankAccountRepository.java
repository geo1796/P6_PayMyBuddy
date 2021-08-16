package com.PayMyBuddy.MoneyTransfer.Repository;

import com.PayMyBuddy.MoneyTransfer.Model.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, String> {
}
