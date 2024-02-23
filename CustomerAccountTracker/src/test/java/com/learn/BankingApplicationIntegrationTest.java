package com.learn;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.entity.Account;
import com.learn.entity.Address;
import com.learn.entity.AddressDTO;
import com.learn.entity.Customer;
import com.learn.entity.CustomerDTO;
import com.learn.entity.CustomerUpdateDTO;
import com.learn.service.CustomerServiceImpl;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CustomerAccountTrackerApplication.class)
public class BankingApplicationIntegrationTest {

    @org.springframework.boot.test.web.server.LocalServerPort
    private int localPort;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    CustomerServiceImpl customerService;

    @Test
    public void testGetAllAccounts() {
        List accounts = testRestTemplate
                .getForObject("http://localhost:" + localPort + "/allaccount", List.class);

        assertThat(accounts.size()).isGreaterThan(1);
    }
    
    
    @Test
    public void testGetAllCustomers() {
        List<Customer> customers = testRestTemplate.getForObject("http://localhost:" + localPort + "/allcustomers", List.class);

        assertThat(customers).isNotEmpty();
    }
    
    @Test
    public void testAddAccount() {
        
        String jsonPayload = "{"
            + "\"accountType\": \"savings\","
            + "\"balance\": 10000.00,"
            + "\"customer\": { \"customerId\": 31 }"
            + "}";

      
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        // Send POST request to the addaccount endpoint
        ResponseEntity<Account> responseEntity = testRestTemplate.postForEntity(
            "http://localhost:" + localPort + "/addaccount",
            requestEntity,
            Account.class
        );

       
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Account addedAccount = responseEntity.getBody();
        assertThat(addedAccount).isNotNull();
       
    }
    
    @Test
    public void testDeleteAccount() {
      
        int accountIdToDelete = 13; 

        // Send DELETE request to the deleteaccount endpoint
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
            "http://localhost:" + localPort + "/deleteaccount/" + accountIdToDelete,
            HttpMethod.DELETE,
            null,
            String.class
        );

      
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Account deleted successfully");
    }
    
    
    @Test
    public void testGetAccountByAccountNumber() {
       
        String accountNumber = "A15214a77b12b"; 

        // Send GET request to the account/{accountNumber} endpoint
        ResponseEntity<Account> responseEntity = testRestTemplate.getForEntity(
            "http://localhost:" + localPort + "/account/" + accountNumber,
            Account.class
        );

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Account account = responseEntity.getBody();
        assertThat(account).isNotNull();
        assertThat(account.getAccountNumber()).isEqualTo(accountNumber);
    }
    
    
    @Test
    public void testTransferFunds() {
        String sourceAccountNumber = "A18f2cf377665";
        String targetAccountNumber = "A15214a77b12b";
        double amount = 500.00; 

        // Create the request body
        String requestUrl = "http://localhost:" + localPort + "/transfer?sourceAccountNumber=" + sourceAccountNumber +
            "&targetAccountNumber=" + targetAccountNumber + "&amount=" + amount;

        // Send POST request to the transfer endpoint
        ResponseEntity<Map<String, Account>> responseEntity = testRestTemplate.exchange(
            requestUrl, HttpMethod.POST, null, new ParameterizedTypeReference<Map<String, Account>>() {});

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Account> updatedAccounts = responseEntity.getBody();
        assertThat(updatedAccounts).isNotNull();
        assertThat(updatedAccounts.get("sourceAccount")).isNotNull();
        assertThat(updatedAccounts.get("targetAccount")).isNotNull();
    }
    
    @Test
    public void testAddCustomer() {
     
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

      
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

       
        HttpEntity<Customer> requestEntity = new HttpEntity<>(newCustomer, headers);

        // Send POST request to the addCustomer endpoint
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
            "http://localhost:" + localPort + "/addcustomer", HttpMethod.POST, requestEntity, String.class);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Customer addedCustomer = objectMapper.readValue(responseEntity.getBody(), Customer.class);
            assertThat(addedCustomer).isNotNull();
            assertThat(addedCustomer.getCustomerId()).isNotNull(); // Assuming id is generated upon creation
            assertThat(addedCustomer.getFirstName()).isEqualTo(newCustomer.getFirstName());
            assertThat(addedCustomer.getLastName()).isEqualTo(newCustomer.getLastName());
            assertThat(addedCustomer.getAddress().getStreet()).isEqualTo(newCustomer.getAddress().getStreet());
            assertThat(addedCustomer.getAddress().getCity()).isEqualTo(newCustomer.getAddress().getCity());
            assertThat(addedCustomer.getAddress().getState()).isEqualTo(newCustomer.getAddress().getState());
            assertThat(addedCustomer.getAddress().getPostalCode()).isEqualTo(newCustomer.getAddress().getPostalCode());
            assertThat(addedCustomer.getContactNumber()).isEqualTo(newCustomer.getContactNumber());
            assertThat(addedCustomer.getEmail()).isEqualTo(newCustomer.getEmail());
            assertThat(addedCustomer.getDateOfBirth()).isEqualTo(newCustomer.getDateOfBirth());

        } catch (Exception e) {
            throw new AssertionError("Failed to parse response JSON: " + e.getMessage());
        }
    }




    @Test
    public void testGetCustomer() {
      
        int customerId = 34;

       
        ResponseEntity<CustomerDTO> responseEntity = testRestTemplate.getForEntity("/customer/" + customerId, CustomerDTO.class);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        CustomerDTO customer = responseEntity.getBody();
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstName()).isEqualTo("John");
        assertThat(customer.getLastName()).isEqualTo("Doe");
        assertThat(customer.getAddress().getStreet()).isEqualTo("456 Updated St");
        assertThat(customer.getAddress().getCity()).isEqualTo("Updated City");
        assertThat(customer.getAddress().getState()).isEqualTo("Updated State");
        assertThat(customer.getAddress().getPostalCode()).isEqualTo("54321");
        assertThat(customer.getContactNumber()).isEqualTo("555-587-6543");
        assertThat(customer.getEmail()).isEqualTo("updated.email@example.com");
       

    }
    

 @Test
 public void testUpdateCustomer() {
   
     int customerId = 34;
    
     CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
     updateDTO.setContactNumber("555-587-6543");
     updateDTO.setEmail("updated.email@example.com");

    
     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON);
     HttpEntity<CustomerUpdateDTO> requestEntity = new HttpEntity<>(updateDTO, headers);

     // Send PUT request to the updateCustomer endpoint
     ResponseEntity<Customer> responseEntity = testRestTemplate.exchange(
             "/updatecustomer/" + customerId,
             HttpMethod.PUT,
             requestEntity,
             Customer.class
     );

     // Verify the response
     assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
     Customer updatedCustomer = responseEntity.getBody();
     System.out.println(updatedCustomer);
     assertThat(updatedCustomer).isNotNull();
     assertThat(updatedCustomer.getContactNumber()).isEqualTo("555-587-6543");
     assertThat(updatedCustomer.getEmail()).isEqualTo("updated.email@example.com");

   
 }
 
 

     @Test
     public void testUpdateAddress() {
         int customerId = 34; // Replace with a valid customer ID
         AddressDTO updatedAddress = new AddressDTO();
         updatedAddress.setStreet("456 Updated St");
         updatedAddress.setCity("Updated City");
         updatedAddress.setState("Updated State");
         updatedAddress.setPostalCode("54321");

         // Send PUT request to update address
         String requestUrl = "http://localhost:" + localPort + "/updateaddress/" + customerId;
         ResponseEntity<Customer> responseEntity = testRestTemplate.exchange(
             requestUrl, HttpMethod.PUT, new HttpEntity<>(updatedAddress), Customer.class);

         // Verify the response
         assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
         Customer updatedCustomer = responseEntity.getBody();
         assertThat(updatedCustomer).isNotNull();
         assertThat(updatedCustomer.getAddress().getStreet()).isEqualTo(updatedAddress.getStreet());
         assertThat(updatedCustomer.getAddress().getCity()).isEqualTo(updatedAddress.getCity());
         assertThat(updatedCustomer.getAddress().getState()).isEqualTo(updatedAddress.getState());
         assertThat(updatedCustomer.getAddress().getPostalCode()).isEqualTo(updatedAddress.getPostalCode());

         // Fetch the customer again to verify the address update
         ResponseEntity<Customer> getResponseEntity = testRestTemplate.getForEntity(
             "http://localhost:" + localPort + "/customer/" + customerId, Customer.class);
         assertThat(getResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
         Customer fetchedCustomer = getResponseEntity.getBody();
         assertThat(fetchedCustomer).isNotNull();
         assertThat(fetchedCustomer.getAddress().getStreet()).isEqualTo(updatedAddress.getStreet());
         assertThat(fetchedCustomer.getAddress().getCity()).isEqualTo(updatedAddress.getCity());
         assertThat(fetchedCustomer.getAddress().getState()).isEqualTo(updatedAddress.getState());
         assertThat(fetchedCustomer.getAddress().getPostalCode()).isEqualTo(updatedAddress.getPostalCode());
     }
     
     @Test
     public void testDeleteCustomer() {
      
         Customer newCustomer = new Customer();
         newCustomer.setFirstName("John");
         newCustomer.setLastName("Doe");
         newCustomer.setAddress(new Address("123 Main St", "Cityville", "Stateville", "12345"));
         newCustomer.setContactNumber("555-123-4567");
         newCustomer.setEmail("john.doe@example.com");
         newCustomer.setDateOfBirth(new Date());
         Customer addedCustomer = customerService.addCustomer(newCustomer);

         // Send DELETE request to delete the customer
         String requestUrl = "http://localhost:" + localPort + "/customer/" + addedCustomer.getCustomerId();
         ResponseEntity<String> responseEntity = testRestTemplate.exchange(
             requestUrl, HttpMethod.DELETE, null, String.class);

         // Verify the response
         assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
         assertThat(responseEntity.getBody()).isEqualTo("Customer deleted successfully");

     }
 





}

