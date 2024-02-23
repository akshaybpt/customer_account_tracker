package com.learn;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.controller.CustomerController;
import com.learn.entity.AddressDTO;
import com.learn.entity.Customer;
import com.learn.entity.CustomerDTO;
import com.learn.entity.CustomerUpdateDTO;
import com.learn.service.CustomerServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {
	
	 @Autowired
	    private MockMvc mockMvc;
	    
	    @MockBean
	    private CustomerServiceImpl customerService;
	    
	    @Test
	    public void testAddCustomer() throws Exception {
	       
	        Customer customer = new Customer();
	        customer.setFirstName("John");
	        customer.setLastName("Doe");
	        customer.setContactNumber("1234567890");
	        customer.setEmail("john@example.com");

	        when(customerService.addCustomer(any(Customer.class))).thenReturn(customer);
	        
	      
	        mockMvc.perform(post("/addcustomer")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(asJsonString(customer))) 
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.firstName").value("John"))
	                .andExpect(jsonPath("$.lastName").value("Doe"))
	                .andExpect(jsonPath("$.contactNumber").value("1234567890"))
	                .andExpect(jsonPath("$.email").value("john@example.com"));
	    }



	    @Test
	    public void testGetCustomer() throws Exception {
	       
	        CustomerDTO customerDTO = new CustomerDTO();
	        customerDTO.setFirstName("John");
	        customerDTO.setLastName("Doe");
	        customerDTO.setContactNumber("1234567890");
	        customerDTO.setEmail("john@example.com");
	        
	        when(customerService.getCustomerWithAccountsById(1)).thenReturn(customerDTO);
	        
	       
	        mockMvc.perform(get("/customer/{id}", 1)
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.firstName").value("John"))
	                .andExpect(jsonPath("$.lastName").value("Doe"))
	                .andExpect(jsonPath("$.contactNumber").value("1234567890"))
	                .andExpect(jsonPath("$.email").value("john@example.com"));
	       
	    }
	    
	@Test
	void testUpdateCustomer() throws Exception {
		CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        updateDTO.setFirstName("UpdatedFirstName");
        updateDTO.setLastName("UpdatedLastName");
        updateDTO.setContactNumber("9876543210");
        updateDTO.setEmail("updated@example.com");
    
        
      
        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("UpdatedFirstName");
        updatedCustomer.setLastName("UpdatedLastName");
        updatedCustomer.setContactNumber("9876543210");
        updatedCustomer.setEmail("updated@example.com");
        // Set other properties as needed
        when(customerService.updateCustomer(eq(1), any(CustomerUpdateDTO.class))).thenReturn(updatedCustomer);

       
        mockMvc.perform(put("/updatecustomer/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("UpdatedFirstName"))
                .andExpect(jsonPath("$.lastName").value("UpdatedLastName"))
                .andExpect(jsonPath("$.contactNumber").value("9876543210"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
       
	}



	    @Test
	    public void testUpdateAddress() throws Exception {
	       
	    	 AddressDTO addressDTO = new AddressDTO();
	         addressDTO.setStreet("123 Updated St");
	         addressDTO.setCity("Updated City");
	         addressDTO.setState("Updated State");
	         addressDTO.setPostalCode("98765");

	     
	         mockMvc.perform(put("/updateaddress/{id}", 1)
	                 .contentType(MediaType.APPLICATION_JSON)
	                 .content(asJsonString(addressDTO)))
	                 .andExpect(status().isOk())
	                 .andReturn();
	     }
	    
	    @Test
	    public void testGetAllCustomersWithAccountsAndAddress() throws Exception {
	        // Prepare some sample customer data
	        CustomerDTO customer1 = new CustomerDTO();
	        customer1.setCustomerId(1);
	        customer1.setFirstName("John");
	       
	        CustomerDTO customer2 = new CustomerDTO();
	        customer2.setCustomerId(2);
	        customer2.setFirstName("Jane");
	      
	        List<CustomerDTO> customers = Arrays.asList(customer1, customer2);

	       
	        when(customerService.getAllCustomersWithAccountsAndAddress()).thenReturn(customers);

	        mockMvc.perform(get("/allcustomers"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$").isArray())
	            .andExpect(jsonPath("$.length()").value(customers.size()))
	            .andExpect(jsonPath("$[0].firstName").value(customer1.getFirstName()))
	            .andExpect(jsonPath("$[1].firstName").value(customer2.getFirstName()));
	          
	    }

	  
	@Test
	void testDeleteCustomer() throws Exception {
		int customerId = 1;
       
        doNothing().when(customerService).deleteCustomer(customerId);

        mockMvc.perform(delete("/customer/{id}", customerId))
            .andExpect(status().isOk())
            .andExpect(content().string("Customer deleted successfully"));

       
        verify(customerService).deleteCustomer(customerId);
    }

	
	
	 private String asJsonString(Object obj) throws Exception {
	        ObjectMapper objectMapper = new ObjectMapper();
	        return objectMapper.writeValueAsString(obj);
	    }

}
