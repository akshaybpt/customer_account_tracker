package com.learn.Exception;

public class InvalidCustomerDetailsException extends RuntimeException {
	
	public  InvalidCustomerDetailsException(String message) {
        super(message);
    }

}
