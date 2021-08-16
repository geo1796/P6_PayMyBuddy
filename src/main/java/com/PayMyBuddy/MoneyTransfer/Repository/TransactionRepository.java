package com.PayMyBuddy.MoneyTransfer.Repository;

import com.PayMyBuddy.MoneyTransfer.Model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
}
