package com.learn.Exception;

public class DuplicateAccountException extends RuntimeException {
	
	public DuplicateAccountException(String message) {
		super(message);
	}

}
