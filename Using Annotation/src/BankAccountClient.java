package com.capgemini.bankapp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import org.apache.log4j.Logger;

import com.capgemini.bankapp.exception.BankAccountNotFoundException;
import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;
import com.capgemini.spring.configuration.AnnotationConfiguration;

public class BankAccountClient {


	static final Logger logger = Logger.getLogger(BankAccountClient.class);



	public static void main(String[] args) {
		int choice;
		long accountId;
		String accountHolderName;
		String accountType;
		double accountBalance;
		double amount;

	
	ApplicationContext context=new ClassPathXmlApplicationContext("Annotation.xml");
	BankAccountServiceImpl bank=context.getBean(BankAccountServiceImpl.class);



		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			while (true) {
				System.out.println(
						"1.Create New Bank Account\n 2.Withdraw\n 3.Deposit\n 4.Fund Transfer\n 5.Delete Bank Account\n 6.Display all bank account details\n 7.Search bank Account\n 8.Check Balance\n 9.Update BankAccount Details\n 10.Exit");
				System.out.println("Enter your choice-");
				choice = Integer.parseInt(reader.readLine());

				switch (choice) {
				case 1:
					System.out.println("Enter account holder name-");
					accountHolderName = reader.readLine();
					System.out.println("Enter account type-");
					accountType = reader.readLine();
					System.out.println("Enter account balance-");
					accountBalance = Double.parseDouble(reader.readLine());

					BankAccount account = new BankAccount(accountHolderName, accountType, accountBalance);
					
					if (bank.addNewBankAccount(account))
						System.out.println("Account created successfully!");
					else
						System.out.println("Account is not created successfully!");
					break;

				case 2:
					System.out.println("Enter your account id-");
					accountId = Long.parseLong(reader.readLine());

					try {
						System.out.println("Your current balance is-" + bank.checkBalance(accountId));
					} catch (BankAccountNotFoundException e) {

						logger.error("Exception", e);
					}
					System.out.println("Enter amount to withdraw-");
					amount = Double.parseDouble(reader.readLine());

					try {
						accountBalance = bank.withdraw(accountId, amount);
						System.out.println("After withdraw your balance is-" + bank.checkBalance(accountId));

					} catch (BankAccountNotFoundException | LowBalanceException e) {
						logger.error("Exception", e);

					}

					break;

				case 3:
					System.out.println("Enter your account id-");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Enter amount to deposit-");
					amount = Double.parseDouble(reader.readLine());
					try {
						accountBalance = bank.deposit(accountId, amount);
						System.out.println("After deposit balance is-" + accountBalance);

					} catch (BankAccountNotFoundException e) {
						logger.error("Exception", e);
					}
					break;

				case 4:
					System.out.println("Enter account id from which you want to transfer-");
					long fromAccountId = Long.parseLong(reader.readLine());
					System.out.println("Enter amount to transfer-");
					amount = Double.parseDouble(reader.readLine());
					System.out.println("Enter account id to which you want to transfer money-");
					long toAccountId = Long.parseLong(reader.readLine());

					try {
						System.out.println(accountBalance = bank.fundTransfer(fromAccountId, toAccountId, amount));
					} catch (LowBalanceException | BankAccountNotFoundException e) {
						logger.error("Exception", e);

					}

					break;

				case 5:
					System.out.println("Enter account id which you want to delete-");
					accountId = Long.parseLong(reader.readLine());
					try {
						if (bank.deleteBankAccount(accountId))

							System.out.println("You have deleted the account");

						else
							System.out.println("Try again!!");
					} catch (BankAccountNotFoundException e) {

						logger.error("Exception", e);
					}

					break;

				case 6:
					System.out.println("Displaying all bank account details-" + bank.findAllBankAccounts());
					break;

				case 7:
					System.out.println("Enter account id to search account");
					accountId = Long.parseLong(reader.readLine());
					try {
						System.out.println(bank.searchAccount(accountId));
					} catch (BankAccountNotFoundException e) {
						logger.error("Exception", e);

					}
					break;

				case 8:
					System.out.println("Enter account id to check balance-");
					accountId = Long.parseLong(reader.readLine());
					try {
						accountBalance = bank.checkBalance(accountId);
						System.out.println("Your balance is-" + accountBalance);

					} catch (BankAccountNotFoundException e) {
						logger.error("Exception", e);
					}
					break;

				case 9:
					System.out.println("Enter account id-");
					accountId = Long.parseLong(reader.readLine());
					try {
						System.out.println(bank.searchAccount(accountId));
					} catch (BankAccountNotFoundException e) {
						logger.error("Exception", e);
					}
					System.out.println("Enter account name to get update-");
					accountHolderName = reader.readLine();
					System.out.println("Enter account type to get update-");
					accountType = reader.readLine();
					try {
						System.out.println(
								bank.updateBankAccountDetails(accountId, accountHolderName, accountType));
					} catch (BankAccountNotFoundException e) {
						logger.error("Exception", e);
					}
					break;

				case 10:
					System.out.println("Thanks for banking with us...!");
					System.exit(0);

				}

			}

		} catch (Exception e) {

			logger.error("Exception", e);
		}


	}

}
