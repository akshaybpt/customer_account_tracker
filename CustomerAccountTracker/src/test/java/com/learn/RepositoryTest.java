package com.learn;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.learn.entity.Account;
import com.learn.entity.Address;
import com.learn.entity.Customer;
import com.learn.repo.AccountRepository;
import com.learn.repo.CustomerRepository;

@SpringBootTest
public class RepositoryTest {

   @Autowired
    private CustomerRepository customerRepository;
   @Autowired
   private AccountRepository accountRepository;
   
   @BeforeEach

    @Test
    public void testSaveForCustomerRepository() {
        // Create a mock customer to be saved
    	Customer newCustomer = new Customer();
    	newCustomer.setFirstName("John");
    	newCustomer.setLastName("Doe");
    	Address address = new Address();
    	address.setStreet("123 Main St");
    	address.setCity("Cityville");
    	address.setState("Stateville");
    	address.setPostalCode("12345");
    	newCustomer.setAddress(address);
    	newCustomer.setContactNumber("555-123-4567");
    	newCustomer.setEmail("john.doe@example.com");
    	newCustomer.setDateOfBirth(new Date());
       
		Customer customer = customerRepository.save(newCustomer);
		
		 assertThat(customer).isNotNull();
	     assertThat(customer.getCustomerId()).isGreaterThan(0);
	
    }
    
//    @Test
//    public void testSaveForAccountRepository() {
//        // Create a mock account to be saved
//    	Account acc=new Account();
//	    acc.setAccountNumber("Abfrfdff342234");
//	    acc.setAccountType("savings");
//	    acc.setBalance(1000.0);
//	    
//	    Account account=accountRepository.save(acc);
//	    
//		assertThat(account).isNotNull();
//	    assertThat(account.getAccountId()).isGreaterThan(0);
//	
//    }
    
    @Test
    public void testFindByIdCustomerRepository() {
    	Customer newCustomer = new Customer();
    	newCustomer.setFirstName("John");
    	newCustomer.setLastName("Doe");
    	Address address = new Address();
    	address.setStreet("123 Main St");
    	address.setCity("Cityville");
    	address.setState("Stateville");
    	address.setPostalCode("12345");
    	newCustomer.setAddress(address);
    	newCustomer.setContactNumber("555-123-4567");
    	newCustomer.setEmail("john.doe@example.com");
    	newCustomer.setDateOfBirth(new Date());
       
		Customer customer = customerRepository.save(newCustomer);
		Customer customerDB=customerRepository.findById(customer.getCustomerId()).get();
		assertThat(customerDB).isNotNull();
    }
    
    
//    @Test
//    public void testFindByIdAccountRepository() {
//    	Account acc=new Account();
//	    acc.setAccountNumber("Abfrff3n4g2234");
//	    acc.setAccountType("savings");
//	    acc.setBalance(1000.0);
//	    
//	    Account account=accountRepository.save(acc);
//		Account accountDB=accountRepository.findById(account.getAccountId()).get();
//		assertThat(accountDB).isNotNull();
//    }
    
    
    @Test
    public void testFindAllAccountRepository() {
    	
     List <Account> accountList=accountRepository.findAll();
     
     assertThat(accountList).isNotNull();
  
    }
    
    @Test
    public void testFindAllCustomerRepository() {
    	
     List <Customer> customerList=customerRepository.findAll();
     
     assertThat(customerList).isNotNull();
  
    }
    
    @Test
    public void testDeleteByIdCustomerRepository() {
    	
    	Customer newCustomer = new Customer();
    	newCustomer.setFirstName("John");
    	newCustomer.setLastName("Doe");
    	Address address = new Address();
    	address.setStreet("123 Main St");
    	address.setCity("Cityville");
    	address.setState("Stateville");
    	address.setPostalCode("12345");
    	newCustomer.setAddress(address);
    	newCustomer.setContactNumber("555-123-4567");
    	newCustomer.setEmail("john.doe@example.com");
    	newCustomer.setDateOfBirth(new Date());
       
		Customer customer = customerRepository.save(newCustomer);
		customerRepository.deleteById(customer.getCustomerId());
		Optional<Customer> customerOptional=customerRepository.findById(customer.getCustomerId());
		assertThat(customerOptional).isEmpty();
    }
    
    
    @Test
    public void testDeleteByIdAccountRepository() {
    	Account acc=new Account();
	    acc.setAccountNumber("A4dsafg234");
	    acc.setAccountType("savings");
	    acc.setBalance(1000.0);
	    
	    Account account=accountRepository.save(acc);
	    accountRepository.deleteById(account.getAccountId());
		Optional<Account> accountOptional=accountRepository.findById(account.getAccountId());
		assertThat(accountOptional).isEmpty();
    }
    
    @Test
    public void testfindByAccountNumber() {
    	
    	Account acc=new Account();
	    acc.setAccountNumber("A4g2gfd");
	    acc.setAccountType("savings");
	    acc.setBalance(1000.0);
	    
	    Account account=accountRepository.save(acc);
	    Account accountDB=accountRepository.findByAccountNumber(account.getAccountNumber()).get();
	    
	    assertThat(accountDB).isNotNull();
    	
    	
    }
    
    
}
