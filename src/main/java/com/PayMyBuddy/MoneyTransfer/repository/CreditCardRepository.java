package com.PayMyBuddy.MoneyTransfer.repository;

import com.PayMyBuddy.MoneyTransfer.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {

    Optional<CreditCard> findByCardNumber(String cardNumber);

}
