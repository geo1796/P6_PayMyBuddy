package com.PayMyBuddy.MoneyTransfer;

import com.PayMyBuddy.MoneyTransfer.service.MyUserDetailsService;
import com.PayMyBuddy.MoneyTransfer.service.RoleService;
import com.PayMyBuddy.MoneyTransfer.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;


@SpringBootApplication
@AllArgsConstructor
public class MoneyTransferApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(MoneyTransferApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) {



	}
}
