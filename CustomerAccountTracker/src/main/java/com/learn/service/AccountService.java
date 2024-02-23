package com.learn.service;

import java.util.List;

import com.learn.entity.Account;

public interface AccountService {
	
	public Account addAccount(Account acc) throws Exception;
	public List<Account> getAllAccounts() throws Exception;
	public Account getAccountByAccountNumber(String accountNumber);

}
