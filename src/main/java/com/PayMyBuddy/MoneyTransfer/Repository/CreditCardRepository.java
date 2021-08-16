package com.PayMyBuddy.MoneyTransfer.Repository;

import com.PayMyBuddy.MoneyTransfer.Model.CreditCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends CrudRepository<CreditCard, String> {
}
