package com.aladdin.youbank001.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    
    public InsufficientBalanceException() {
        super("Balance is not enough.");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
