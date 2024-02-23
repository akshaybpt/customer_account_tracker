package com.learn.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.learn.Exception.AccountNotFoundException;
import com.learn.Exception.InsufficientFundsException;
import com.learn.entity.Account;
import com.learn.service.AccountServiceImpl;
import com.learn.service.CustomerServiceImpl;

@RestController
public class AccountController {

	
	@Autowired
	AccountServiceImpl accountService;
	
	@Autowired
	CustomerServiceImpl customerService;
	
	 @PostMapping("/addaccount")
	    public ResponseEntity<Object> addAccount(@RequestBody Account acc) throws Exception {
	        try {
	            Account addedAccount = accountService.addAccount(acc);
	            return new ResponseEntity<>(addedAccount, HttpStatus.CREATED);
	        } catch (Exception e) {
	            throw e;
	        }
	        
	 }
	 
	 @DeleteMapping("/deleteaccount/{accountId}")
	    public ResponseEntity<String> deleteAccount(@PathVariable int accountId) throws Exception {
	        try {
	            accountService.deleteAccount(accountId);
	            return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
	        }  catch (Exception e) {
	          throw e;
	        }
	    }

	 
	 @GetMapping("/allaccount")
	    public ResponseEntity<List<Account>> getAllAccounts() throws Exception {
	        try {
	            List<Account> accounts = accountService.getAllAccounts();
	            return new ResponseEntity<>(accounts, HttpStatus.OK);
	        } catch (Exception e) {
	            throw e;
	        }
	    }
	 
	 
	  @GetMapping("/account/{accountNumber}")
	    public ResponseEntity<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
	        try {
	            Account account = accountService.getAccountByAccountNumber(accountNumber);
	            return new ResponseEntity<>(account, HttpStatus.OK);
	        } catch (Exception e) {
	           throw e;
	        }
	    }
	 
	  @PostMapping("/transfer")
	    public ResponseEntity<Map<String, Account>> transferFunds(
	            @RequestParam String sourceAccountNumber,
	            @RequestParam String targetAccountNumber,
	            @RequestParam double amount) {
	        try {
	            Map<String, Account> updatedAccounts = accountService.transferFunds(sourceAccountNumber, targetAccountNumber, amount);
	            return new ResponseEntity<>(updatedAccounts, HttpStatus.OK);
	        } catch (Exception e) {
	            throw e;
	        }
	    }
}
