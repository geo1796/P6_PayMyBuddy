package com.PayMyBuddy.MoneyTransfer.repository;

import com.PayMyBuddy.MoneyTransfer.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    Optional<BankAccount> findByIban(String iban);

}
