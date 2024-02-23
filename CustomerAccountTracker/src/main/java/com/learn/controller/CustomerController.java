package com.learn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.learn.Exception.CustomerNotFoundException;
import com.learn.Exception.InvalidCustomerDetailsException;
import com.learn.entity.AddressDTO;
import com.learn.entity.Customer;
import com.learn.entity.CustomerDTO;
import com.learn.entity.CustomerUpdateDTO;
import com.learn.service.CustomerServiceImpl;

@RestController
public class CustomerController {
	
	@Autowired
	CustomerServiceImpl customerService;
	
	@PostMapping("/addcustomer")
	public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
	    try {
	        Customer result = customerService.addCustomer(customer);
	        return new ResponseEntity<>(result, HttpStatus.CREATED);
	    } catch (Exception e) {
	        throw e; // Let the GlobalExceptionHandler handle this exception
	    }
	}

	
	@GetMapping("/customer/{id}")
	public ResponseEntity<?> getCustomer(@PathVariable int id) throws Exception {
	    try {
	        CustomerDTO c = customerService.getCustomerWithAccountsById(id);
	            return new ResponseEntity<>(c, HttpStatus.OK);
	     }catch (Exception e) {
          throw e; // Let the GlobalExceptionHandler handle this exception
        }
	}
	
	@PutMapping("/updatecustomer/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable int id, @RequestBody CustomerUpdateDTO updateDTO) throws Exception {
	    try {
	        Customer updatedCustomer = customerService.updateCustomer(id, updateDTO);
	        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
	    } catch (Exception e) {
	       throw e;  // Let the GlobalExceptionHandler handle this exception
	    }
	}
	
	@PutMapping("/updateaddress/{id}")
    public ResponseEntity<Object> updateAddress(@PathVariable int id, @RequestBody AddressDTO addressDTO) throws Exception {
        try {
            customerService.updateCustomerAddress(id, addressDTO);
           Customer customer = customerService.getCustomerById(id);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (Exception e) {
           throw e;      // Let the GlobalExceptionHandler handle this exception   
           }
    }


	@GetMapping("/allcustomers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomersWithAccountsAndAddress() throws Exception {
        try {
            List<CustomerDTO> customers = customerService.getAllCustomersWithAccountsAndAddress();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
           throw e;
        }
    }
	
	
	@DeleteMapping("/customer/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable int id) throws Exception {
        try {
            customerService.deleteCustomer(id);
            return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
           throw e;  // Let the GlobalExceptionHandler handle this exception
        } 
    }


}
