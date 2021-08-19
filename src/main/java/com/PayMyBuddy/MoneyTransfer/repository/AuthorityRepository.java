package com.PayMyBuddy.MoneyTransfer.repository;

import com.PayMyBuddy.MoneyTransfer.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
