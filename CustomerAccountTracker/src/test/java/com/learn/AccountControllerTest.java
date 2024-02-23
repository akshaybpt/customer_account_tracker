package com.learn;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.learn.controller.AccountController;
import com.learn.entity.Account;
import com.learn.service.AccountServiceImpl;
import com.learn.service.CustomerServiceImpl;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountServiceImpl accountService;
    
    @MockBean
    private CustomerServiceImpl customerService;


    @Test
    public void testAddAccount() throws Exception {
        Account newAccount = new Account();
        newAccount.setAccountNumber("A12345");
        newAccount.setAccountType("Savings");
        newAccount.setBalance(1000.0);

        when(accountService.addAccount(any(Account.class))).thenReturn(newAccount);

        mockMvc.perform(post("/addaccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountNumber\":\"A12345\",\"accountType\":\"Savings\",\"balance\":1000.0}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value("A12345"))
                .andExpect(jsonPath("$.accountType").value("Savings"))
                .andExpect(jsonPath("$.balance").value(1000.0));

        verify(accountService).addAccount(any(Account.class));
    }

    @Test
    public void testDeleteAccount() throws Exception {
        int accountIdToDelete = 101;

        mockMvc.perform(delete("/deleteaccount/{accountId}", accountIdToDelete))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted successfully"));

        verify(accountService, times(1)).deleteAccount(accountIdToDelete);
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        Account account1 = new Account();
        account1.setAccountNumber("A123");
        account1.setAccountType("Savings");
        account1.setBalance(1000.0);

        Account account2 = new Account();
        account2.setAccountNumber("A456");
        account2.setAccountType("Checking");
        account2.setBalance(2000.0);

        List<Account> accounts = Arrays.asList(account1, account2);

        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/allaccount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].accountNumber").value("A123"))
                .andExpect(jsonPath("$[0].accountType").value("Savings"))
                .andExpect(jsonPath("$[0].balance").value(1000.0))
                .andExpect(jsonPath("$[1].accountNumber").value("A456"))
                .andExpect(jsonPath("$[1].accountType").value("Checking"))
                .andExpect(jsonPath("$[1].balance").value(2000.0));
    }

    @Test
    public void testGetAccountByAccountNumber() throws Exception {
        Account account = new Account();
        account.setAccountNumber("A123");
        account.setAccountType("Savings");
        account.setBalance(1000.0);

        when(accountService.getAccountByAccountNumber("A123")).thenReturn(account);

        mockMvc.perform(get("/account/{accountNumber}", "A123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value("A123"))
                .andExpect(jsonPath("$.accountType").value("Savings"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

	@Test
	void testTransferFunds() throws Exception {
		  Account sourceAccount = new Account();
	        sourceAccount.setAccountNumber("A123");
	        sourceAccount.setBalance(1000.0);

	        Account targetAccount = new Account();
	        targetAccount.setAccountNumber("B456");
	        targetAccount.setBalance(500.0);

	        when(accountService.transferFunds(anyString(), anyString(), anyDouble()))
	                .thenReturn(Map.of("sourceAccount", sourceAccount, "targetAccount", targetAccount));

	        mockMvc.perform(post("/transfer")
	                .param("sourceAccountNumber", "A123")
	                .param("targetAccountNumber", "B456")
	                .param("amount", "200.0"))
	                .andExpect(status().isOk())
	                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	                .andExpect(jsonPath("$.sourceAccount.accountNumber").value("A123"))
	                .andExpect(jsonPath("$.sourceAccount.balance").value(1000.0))
	                .andExpect(jsonPath("$.targetAccount.accountNumber").value("B456"))
	                .andExpect(jsonPath("$.targetAccount.balance").value(500.0));
	    
	}

}
