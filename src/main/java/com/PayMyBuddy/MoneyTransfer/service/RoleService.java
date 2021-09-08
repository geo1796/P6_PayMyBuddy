package com.PayMyBuddy.MoneyTransfer.service;

import com.PayMyBuddy.MoneyTransfer.model.Role;
import com.PayMyBuddy.MoneyTransfer.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    public Iterable<Role> getRoles(){ return roleRepository.findAll(); }

}
