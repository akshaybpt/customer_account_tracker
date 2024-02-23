package com.learn;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import com.learn.entity.Account;
import com.learn.entity.AccountDTO;
import com.learn.entity.Address;
import com.learn.entity.AddressDTO;
import com.learn.entity.Customer;
import com.learn.entity.CustomerDTO;
import com.learn.entity.CustomerUpdateDTO;
import com.learn.repo.CustomerRepository;
import com.learn.service.CustomerServiceImpl;

@SpringBootTest
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCustomer() throws Exception {
       
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setDateOfBirth(new Date()); 

       
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

       
        Customer addedCustomer = customerService.addCustomer(customer);

      
        assertNotNull(addedCustomer);
        assertEquals("John", addedCustomer.getFirstName());
        assertEquals("Doe", addedCustomer.getLastName());
       
    }


    @Test
    void testGetCustomerById() throws Exception {
       
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
       
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        Customer foundCustomer = customerService.getCustomerById(1);

        assertNotNull(foundCustomer);
        assertEquals(1, foundCustomer.getCustomerId());
        assertEquals("John", foundCustomer.getFirstName());
        assertEquals("Doe", foundCustomer.getLastName());
    }
    
    @Test
    void testUpdateCustomer() throws Exception {
       
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1);
        existingCustomer.setFirstName("John");
        existingCustomer.setLastName("Doe");

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        updateDTO.setFirstName("Updated First Name");
        updateDTO.setLastName("Updated Last Name");

       
        when(customerRepository.findById(1)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        Customer updatedCustomer = customerService.updateCustomer(1, updateDTO);


        assertNotNull(updatedCustomer);
        assertEquals("Updated First Name", updatedCustomer.getFirstName());
        assertEquals("Updated Last Name", updatedCustomer.getLastName());
    }
    
    
    

        @Test
        void testGetCustomerWithAccounts() throws Exception {
        
            Address address = new Address();
            address.setAddressId(1);
            address.setStreet("123 Main St");
            address.setCity("City1");
            address.setState("State1");
            address.setPostalCode("12345");

            Account account = new Account();
            account.setAccountId(1);
            account.setAccountType("savings");
            account.setAccountNumber("A12345");
            account.setBalance(1000.0);

            Customer customer = new Customer();
            customer.setCustomerId(1);
            customer.setFirstName("John");
            customer.setLastName("Doe");
            customer.setContactNumber("555-123-4567");
            customer.setEmail("john.doe@example.com");
            customer.setDateOfBirth(new Date());
            customer.setAddress(address);
            customer.setAccounts(Collections.singletonList(account));

           
            when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

           
            CustomerDTO customerDTO = customerService.getCustomerWithAccountsById(1);

           
            assertNotNull(customerDTO);
            assertEquals("John", customerDTO.getFirstName());
            assertEquals("Doe", customerDTO.getLastName());
            assertEquals(1, customerDTO.getAddress().getAddressId());
            assertEquals(1, customerDTO.getAccounts().size());
            assertEquals("A12345", customerDTO.getAccounts().get(0).getAccountNumber());
        }
        
        @Test
        void testDeleteCustomer() throws Exception {
           
            Customer customer = new Customer();
            customer.setCustomerId(1);
            when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
            assertDoesNotThrow(() -> customerService.deleteCustomer(1));
            verify(customerRepository, times(1)).delete(customer);
        }
        
        
        
        
       

            @Test
            void testUpdateCustomerAddress_ValidId() throws Exception {
             
                Customer customer = new Customer();
                Address address = new Address();
                customer.setAddress(address);
                customer.setCustomerId(1);

                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setStreet("New Street");
                addressDTO.setCity("New City");
                addressDTO.setState("New State");
                addressDTO.setPostalCode("12345");

               
                when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

               
                assertDoesNotThrow(() -> customerService.updateCustomerAddress(1, addressDTO));

               
                assertEquals(addressDTO.getStreet(), address.getStreet());
                assertEquals(addressDTO.getCity(), address.getCity());
                assertEquals(addressDTO.getState(), address.getState());
                assertEquals(addressDTO.getPostalCode(), address.getPostalCode());

               
                verify(customerRepository, times(1)).save(customer);
            }


                @Test
                void testGetAllCustomersWithAccountsAndAddress() throws Exception {
                  
                    Customer customer = new Customer();
                    Address address = new Address();
                    Account account = new Account();
                    customer.setCustomerId(1);
                    customer.setFirstName("John");
                    customer.setAddress(address);
                    customer.addAccount(account);

                   
                    when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));

                  
                    List<CustomerDTO> customerDTOs = customerService.getAllCustomersWithAccountsAndAddress();

                    
                    assertEquals(1, customerDTOs.size());
                    CustomerDTO customerDTO = customerDTOs.get(0);
                    assertEquals(1, customerDTO.getCustomerId());
                    assertEquals("John", customerDTO.getFirstName());

                   
                    AddressDTO addressDTO = customerDTO.getAddress();
                    assertNotNull(addressDTO);
                    assertEquals(address.getAddressId(), addressDTO.getAddressId());

                    
                    List<AccountDTO> accountDTOs = customerDTO.getAccounts();
                    assertEquals(1, accountDTOs.size());
                    AccountDTO accountDTO = accountDTOs.get(0);
                    assertEquals(account.getAccountId(), accountDTO.getAccountId());

                   
                    verify(customerRepository, times(1)).findAll();
                }
            

}
