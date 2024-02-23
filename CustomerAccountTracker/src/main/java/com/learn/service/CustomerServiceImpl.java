package com.learn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.learn.Exception.CustomerNotFoundException;
import com.learn.Exception.InvalidCustomerDetailsException;
import com.learn.entity.Account;
import com.learn.entity.AccountDTO;
import com.learn.entity.Address;
import com.learn.entity.AddressDTO;
import com.learn.entity.Customer;
import com.learn.entity.CustomerDTO;
import com.learn.entity.CustomerUpdateDTO;
import com.learn.repo.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	
	@Autowired
	CustomerRepository customerRepository;

	// add customer 
	 public Customer addCustomer(Customer customer) throws InvalidCustomerDetailsException {
	        if (customer == null || customer.getFirstName() == null || customer.getFirstName().isEmpty() ||
	            customer.getLastName() == null || customer.getLastName().isEmpty() ||
	            customer.getDateOfBirth() == null) {
	            throw new InvalidCustomerDetailsException("Invalid customer details");
	        } else {
	            Address address = customer.getAddress();
	            if (address != null) {
	                address.setCustomer(customer); 
	            }
	            return customerRepository.save(customer);
	        }
	    }
	 
	 // update customer 
	 public Customer updateCustomer(int id, CustomerUpdateDTO updateDTO) throws Exception {
		    Optional<Customer> optionalCustomer = customerRepository.findById(id);
		    
		    if (optionalCustomer.isPresent()) {
		        Customer customer = optionalCustomer.get();
		        
		        if (updateDTO.getFirstName() != null) {
		            customer.setFirstName(updateDTO.getFirstName());
		        }
		        
		        if (updateDTO.getLastName() != null) {
		            customer.setLastName(updateDTO.getLastName());
		        }
		        
		        if (updateDTO.getContactNumber() != null) {
		            customer.setContactNumber(updateDTO.getContactNumber());
		        }
		        
		        if (updateDTO.getEmail() != null) {
		            customer.setEmail(updateDTO.getEmail());
		        }
		        
		        if (updateDTO.getDateOfBirth() != null) {
		            customer.setDateOfBirth(updateDTO.getDateOfBirth());
		        }
		        
		        return customerRepository.save(customer);
		    } else {
		        throw new CustomerNotFoundException("Customer with ID " + id + " not found");
		    }
		}


	
	 public Customer getCustomerById(int id) throws Exception {
		    Optional<Customer> customer = customerRepository.findById(id);
		    if (customer.isPresent()) {
		        return customer.get();
		    } else {
		        throw new CustomerNotFoundException("Customer with ID " + id + " not found");
		    }
		}
	 
	 // get the customer and the account list
	 public CustomerDTO getCustomerWithAccountsById(int id) throws Exception {
		    Optional<Customer> customer = customerRepository.findById(id);
		    if (customer.isPresent()) {
		        CustomerDTO customerDTO = new CustomerDTO();
		        Customer actualCustomer = customer.get();

		        customerDTO.setCustomerId(actualCustomer.getCustomerId());
		        customerDTO.setFirstName(actualCustomer.getFirstName());
		        customerDTO.setLastName(actualCustomer.getLastName());
		        customerDTO.setContactNumber(actualCustomer.getContactNumber());
		        customerDTO.setEmail(actualCustomer.getEmail());
		        customerDTO.setDateOfBirth(actualCustomer.getDateOfBirth());

		        AddressDTO addressDTO = new AddressDTO();
		        Address actualAddress = actualCustomer.getAddress();
		        addressDTO.setAddressId(actualAddress.getAddressId());
		        addressDTO.setStreet(actualAddress.getStreet());
		        addressDTO.setCity(actualAddress.getCity());
		        addressDTO.setState(actualAddress.getState());
		        addressDTO.setPostalCode(actualAddress.getPostalCode());
		        customerDTO.setAddress(addressDTO);

		        List<AccountDTO> accountDTOs = new ArrayList<>();
		        for (Account account : actualCustomer.getAccounts()) {
		            AccountDTO accountDTO = new AccountDTO();
		            accountDTO.setAccountId(account.getAccountId());
		            accountDTO.setAccountType(account.getAccountType());
		            accountDTO.setAccountNumber(account.getAccountNumber());
		            accountDTO.setBalance(account.getBalance());
		            accountDTOs.add(accountDTO);
		        }
		        customerDTO.setAccounts(accountDTOs);

		        return customerDTO;
		    } else {
		        throw new CustomerNotFoundException("Customer with ID " + id + " not found");
		    }
		}
	 
	 
	 // delete the customer
	    public void deleteCustomer(int id) throws CustomerNotFoundException {
	        Optional<Customer> customer = customerRepository.findById(id);
	        if (customer.isPresent()) {
	            customerRepository.delete(customer.get());
	        } else {
	            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
	        }
	    }

	    // update the customer address 
	    public void updateCustomerAddress(int id, AddressDTO addressDTO) throws Exception {
	        Optional<Customer> customerOptional = customerRepository.findById(id);
	        if (customerOptional.isPresent()) {
	            Customer customer = customerOptional.get();
	            Address address = customer.getAddress();
	            if (addressDTO.getStreet() != null) {
	                address.setStreet(addressDTO.getStreet());
	            }
	            if (addressDTO.getCity() != null) {
	                address.setCity(addressDTO.getCity());
	            }
	            if (addressDTO.getState() != null) {
	                address.setState(addressDTO.getState());
	            }
	            if (addressDTO.getPostalCode() != null) {
	                address.setPostalCode(addressDTO.getPostalCode());
	            }
	            customerRepository.save(customer);
	        } else {
	            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
	        }
	    }

	    
	    public List<CustomerDTO> getAllCustomersWithAccountsAndAddress() throws Exception {
	        List<Customer> customers = customerRepository.findAll();
	        List<CustomerDTO> customerDTOs = new ArrayList<>();

	        for (Customer customer : customers) {
	            CustomerDTO customerDTO = new CustomerDTO();
	            customerDTO.setCustomerId(customer.getCustomerId());
	            customerDTO.setFirstName(customer.getFirstName());
	            customerDTO.setLastName(customer.getLastName());
	            customerDTO.setContactNumber(customer.getContactNumber());
	            customerDTO.setEmail(customer.getEmail());
	            customerDTO.setDateOfBirth(customer.getDateOfBirth());

	            Address address = customer.getAddress();
	            AddressDTO addressDTO = new AddressDTO();
	            addressDTO.setAddressId(address.getAddressId());
	            addressDTO.setStreet(address.getStreet());
	            addressDTO.setCity(address.getCity());
	            addressDTO.setState(address.getState());
	            addressDTO.setPostalCode(address.getPostalCode());

	            customerDTO.setAddress(addressDTO);

	            List<AccountDTO> accountDTOs = new ArrayList<>();
	            for (Account account : customer.getAccounts()) {
	                AccountDTO accountDTO = new AccountDTO();
	                accountDTO.setAccountId(account.getAccountId());
	                accountDTO.setAccountType(account.getAccountType());
	                accountDTO.setAccountNumber(account.getAccountNumber());
	                accountDTO.setBalance(account.getBalance());
	                accountDTOs.add(accountDTO);
	            }
	            customerDTO.setAccounts(accountDTOs);

	            customerDTOs.add(customerDTO);
	        }

	       
	        if (customerDTOs.isEmpty()) {
	            throw new CustomerNotFoundException("No Customer found");
	        }else {
	        	 return customerDTOs;
	        }
	    }


}
