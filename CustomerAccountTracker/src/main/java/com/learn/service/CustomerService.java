package com.learn.service;

import java.util.List;

import com.learn.Exception.CustomerNotFoundException;
import com.learn.entity.AddressDTO;
import com.learn.entity.Customer;
import com.learn.entity.CustomerDTO;

public interface CustomerService {
	
	public Customer addCustomer(Customer c) throws Exception;
	public List<CustomerDTO> getAllCustomersWithAccountsAndAddress()  throws Exception;
	public Customer getCustomerById(int id) throws Exception;
	public void updateCustomerAddress(int id, AddressDTO addressDTO) throws Exception;
	public void deleteCustomer(int id) throws CustomerNotFoundException;
	public CustomerDTO getCustomerWithAccountsById(int id) throws Exception ;

}
