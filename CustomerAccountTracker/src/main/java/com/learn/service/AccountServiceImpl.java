package com.learn.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.Exception.AccountNotFoundException;
import com.learn.Exception.CustomerNotFoundException;
import com.learn.Exception.DuplicateAccountException;
import com.learn.Exception.InsufficientFundsException;
import com.learn.Exception.InvalidAccountDetailsException;
import com.learn.Exception.InvalidAccountTypeException;

import com.learn.entity.Account;
import com.learn.entity.Customer;
import com.learn.repo.AccountRepository;
import com.learn.repo.CustomerRepository;


@Service
public class AccountServiceImpl implements AccountService {
	
	
	@Autowired
	AccountRepository accountRepository;
	@Autowired
    CustomerServiceImpl customerService;
	@Autowired
	CustomerRepository customerRepository;
	
	// add a account
	  public Account addAccount(Account acc) throws Exception {
	        if (acc == null || acc.getAccountType() == null || acc.getAccountType().isEmpty() ||
	                acc.getBalance() < 0 || acc.getCustomer() == null || acc.getCustomer().getCustomerId() <= 0) {
	            throw new InvalidAccountDetailsException("Invalid account details");
	        }

	        if (!isValidAccountType(acc.getAccountType())) {
	            throw new InvalidAccountTypeException("Invalid account type");
	        }

	        Customer customer = customerService.getCustomerById(acc.getCustomer().getCustomerId());
	        if (customer == null) {
	            throw new CustomerNotFoundException("Customer not found");
	        }

	        if ("savings".equalsIgnoreCase(acc.getAccountType()) && customer.hasSavingsAccount()) {
	            throw new DuplicateAccountException("Customer already has a savings account");
	        }

	        if ("current".equalsIgnoreCase(acc.getAccountType()) && customer.hasCurrentAccount()) {
	            throw new DuplicateAccountException("Customer already has a current account");
	        }

	        if ("salary".equalsIgnoreCase(acc.getAccountType()) && customer.hasSalaryAccount()) {
	            throw new DuplicateAccountException("Customer already has a salary account");
	        }

	        // Generate a random account number
	        String accountNumber = generateRandomAccountNumber();
	        acc.setAccountNumber(accountNumber);
	        
	       Customer c= customerService.getCustomerById(acc.getCustomer().getCustomerId());
	       
	        Account addedAccount = accountRepository.save(acc);
	        addedAccount.setCustomer(c);
	        return addedAccount;
	    }
		

	private String generateRandomAccountNumber() {
	    return "A" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
	}
	
	// delete a account
	 public void deleteAccount(int accountId) throws AccountNotFoundException {
	        Optional<Account> optionalAccount = accountRepository.findById(accountId);
	        if (optionalAccount.isPresent()) {
	            Account account = optionalAccount.get();
	            Customer customer = account.getCustomer();
	            if (customer != null) {
	                customer.removeAccount(account);
	                customerRepository.save(customer);
	            }
	            accountRepository.delete(account);
	        } else {
	            throw new AccountNotFoundException("Account with ID " + accountId + " not found");
	        }
	    }

	// get the list of accounts
	 public List<Account> getAllAccounts() throws Exception {
	        List<Account> accounts = accountRepository.findAll();
	        if (accounts.isEmpty()) {
	            throw new AccountNotFoundException("No accounts found");
	        }
	        return accounts;
	    }
	 
	 
	 // get the account by the account number
	 public Account getAccountByAccountNumber(String accountNumber) {
	        Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
	        if (accountOptional.isPresent()) {
	            return accountOptional.get();
	        } else {
	            throw new AccountNotFoundException("Account not found");
	        }
	    }

         // transfer the fund using the account numbers
	     public Map<String, Account> transferFunds(String sourceAccountNumber, String targetAccountNumber, double amount)
	             throws AccountNotFoundException, InsufficientFundsException {
	         if (amount <= 0) {
	             throw new IllegalArgumentException("Invalid transfer amount");
	         }

	         Optional<Account> sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
	         Optional<Account> targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);

	         if (sourceAccount == null || targetAccount == null) {
	             throw new AccountNotFoundException("Account not found");
	         }

	         if (sourceAccount.get().getBalance() < amount) {
	             throw new InsufficientFundsException("Insufficient funds in source account");
	         }

	         sourceAccount.get().setBalance(sourceAccount.get().getBalance() - amount);
	         targetAccount.get().setBalance(targetAccount.get().getBalance() + amount);

	         Account updatedSourceAccount = accountRepository.save(sourceAccount.get());
	         Account updatedTargetAccount = accountRepository.save(targetAccount.get());

	         Map<String, Account> updatedAccounts = new HashMap<>();
	         updatedAccounts.put("sourceAccount", updatedSourceAccount);
	         updatedAccounts.put("targetAccount", updatedTargetAccount);

	         return updatedAccounts;
	     }
	 
	   
	   private boolean isValidAccountType(String accountType) {
		    List<String> validAccountTypes = Arrays.asList("savings", "current", "salary");
		    String accountTypeLower = accountType.toLowerCase();
		    return validAccountTypes.contains(accountTypeLower);
		}




	

}