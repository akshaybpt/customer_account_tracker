package com.learn.Exception;

public class InvalidAccountTypeException extends RuntimeException {
	
	public  InvalidAccountTypeException(String message) {
        super(message);
    }

}
