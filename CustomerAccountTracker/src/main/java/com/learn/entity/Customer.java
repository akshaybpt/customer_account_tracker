package com.learn.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Account> accounts = new ArrayList<>();

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

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

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	 public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	

	@Override
	public String toString() {
	    return "Customer [customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName
	            + ", contactNumber=" + contactNumber + ", email=" + email + ", dateOfBirth=" + dateOfBirth + "]";
	}
	
	 public void removeAccount(Account account) {
	        account.setCustomer(null);
	        accounts.remove(account);
	    }


	public boolean hasSavingsAccount() {
	        for (Account account : accounts) {
	            if ("savings".equalsIgnoreCase(account.getAccountType())) {
	                return true;
	            }
	        }
	        return false;
	    }
	 
	  public boolean hasCurrentAccount() {
	        for (Account account : accounts) {
	            if ("current".equalsIgnoreCase(account.getAccountType())) {
	                return true;
	            }
	        }
	        return false;
	    }
	  
	  
	  public boolean hasSalaryAccount() {
	        for (Account account : accounts) {
	            if ("salary".equalsIgnoreCase(account.getAccountType())) {
	                return true;
	            }
	        }
	        return false;
	    }
	  
	  public void addAccount(Account account) {
	        accounts.add(account);
	        account.setCustomer(this);
	    }

  
}

