package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.model.Authority;
import com.PayMyBuddy.MoneyTransfer.repository.AuthorityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorityService {

    private AuthorityRepository authorityRepository;

    public Iterable<Authority> getAuthorities(){ return authorityRepository.findAll(); }
}
