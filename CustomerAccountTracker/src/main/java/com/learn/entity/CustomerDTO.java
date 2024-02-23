package com.learn.entity;

import java.util.Date;
import java.util.List;

public class CustomerDTO {
    private int customerId;
    private String firstName;
    private String lastName;
    private AddressDTO address; 
    private String contactNumber;
    private String email;
    private Date dateOfBirth;
    private List<AccountDTO> accounts;
    
    
    
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public AddressDTO getAddress() {
		return address;
	}
	public void setAddress(AddressDTO address) {
		this.address = address;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public List<AccountDTO> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountDTO> accounts) {
		this.accounts = accounts;
	}
    
}
