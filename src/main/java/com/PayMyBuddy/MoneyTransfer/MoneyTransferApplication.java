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

	private MyUserDetailsService myUserDetailsService;
	private TransactionService transactionService;
	private RoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(MoneyTransferApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) {

		transactionService.getTransactions().forEach(
				transaction -> System.out.println("receiver : " + transaction.getReceiver().getEmail()
				+ " sender : " + transaction.getSender().getEmail())
		);

		myUserDetailsService.getAllUsers().forEach(
				user -> System.out.println("transactionAsSender : " + user.getTransactionsAsReceiver().size()
				+ " transactionAsReceiver : " + user.getTransactionsAsSender().size()
				+ " CreditCard : " + user.getCreditCards().size()
				+ " BankAccount : " + user.getBankAccounts().size()
				+ " Role : " + user.getRoles().size())
		);

	}
}
