package com.learn;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.learn.Exception.AccountNotFoundException;
import com.learn.Exception.InsufficientFundsException;
import com.learn.entity.Account;
import com.learn.entity.Customer;
import com.learn.repo.AccountRepository;
import com.learn.repo.CustomerRepository;
import com.learn.service.AccountService;
import com.learn.service.AccountServiceImpl;
import com.learn.service.CustomerServiceImpl;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerServiceImpl customerService;

    @Test
    public void testAddAccount() throws Exception {
        Account accountToAdd = new Account();
        accountToAdd.setAccountType("savings");
        accountToAdd.setBalance(1000.0);
        Customer customer = new Customer();
        customer.setCustomerId(123);
        accountToAdd.setCustomer(customer);

        when(customerService.getCustomerById(customer.getCustomerId())).thenReturn(customer);
        when(accountRepository.save(any(Account.class))).thenReturn(accountToAdd);

        Account addedAccount = accountService.addAccount(accountToAdd);

        assertNotNull(addedAccount);
        assertEquals(accountToAdd.getAccountType(), addedAccount.getAccountType());
        assertEquals(accountToAdd.getBalance(), addedAccount.getBalance(), 0.001);
        assertEquals(accountToAdd.getCustomer(), addedAccount.getCustomer());
    }

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Test
    public void testDeleteAccount() throws AccountNotFoundException {
        Account account = new Account();
        account.setAccountId(1);
        Customer customer = new Customer();
        customer.addAccount(account);

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        accountService.deleteAccount(1);

        verify(accountRepository, times(1)).delete(account);
        verify(customerRepository, times(1)).save(customerCaptor.capture());

        Customer capturedCustomer = customerCaptor.getValue();
        assertTrue(capturedCustomer.getAccounts().isEmpty()); 
    }

    @Test
    void testGetAllAccounts() throws Exception {
        // Create a list of accounts for mocking repository response
        List<Account> accountList = new ArrayList<>();
        Account account1 = new Account();
        account1.setAccountId(1);
        account1.setAccountType("savings");
        account1.setBalance(1000.0);

        Account account2 = new Account();
        account2.setAccountId(2);
        account2.setAccountType("current");
        account2.setBalance(2000.0);
        
        accountList.add(account1);
        accountList.add(account2);

        when(accountRepository.findAll()).thenReturn(accountList);

        List<Account> retrievedAccounts = accountService.getAllAccounts();

        assertNotNull(retrievedAccounts);
        assertEquals(accountList.size(), retrievedAccounts.size());
    }

    @Test
    void testGetAccountByAccountNumber() {
        String accountNumber = "A123";
        Account mockAccount = new Account();
        mockAccount.setAccountId(1);
        mockAccount.setAccountNumber(accountNumber);
        mockAccount.setAccountType("savings");
        mockAccount.setBalance(1000.0);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(mockAccount));

        Account retrievedAccount = accountService.getAccountByAccountNumber(accountNumber);

        assertNotNull(retrievedAccount);
        assertEquals(accountNumber, retrievedAccount.getAccountNumber());
        assertEquals("savings", retrievedAccount.getAccountType());
        assertEquals(1000.0, retrievedAccount.getBalance());

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

   
 

    @Test
    void testTransferFunds() throws AccountNotFoundException, InsufficientFundsException {
        String sourceAccountNumber = "A12b";
        String targetAccountNumber = "A165";

        Account account1 = new Account();
        account1.setAccountId(1);
        account1.setAccountNumber(sourceAccountNumber);
        account1.setAccountType("savings");
        account1.setBalance(1000.00);

        Account account2 = new Account();
        account2.setAccountId(2);
        account2.setAccountNumber(targetAccountNumber);
        account2.setAccountType("savings");
        account2.setBalance(1000.00);

        // Stub repository behavior for source and target accounts
        when(accountRepository.findByAccountNumber(sourceAccountNumber)).thenReturn(Optional.of(account1));
        when(accountRepository.findByAccountNumber(targetAccountNumber)).thenReturn(Optional.of(account2));

        // Stub repository behavior for saving the updated accounts
        when(accountRepository.save(any(Account.class))).thenReturn(account1, account2);

        Map<String, Account> updatedAccounts = accountService.transferFunds(sourceAccountNumber, targetAccountNumber, 200.0);

        verify(accountRepository, times(2)).save(any(Account.class));

        assertEquals(800.0, updatedAccounts.get("sourceAccount").getBalance());
        assertEquals(1200.0, updatedAccounts.get("targetAccount").getBalance());
    }



}
