package com.example.springbootapp.exceptions;

import java.math.BigDecimal;

public class InsufficientAmountException extends RuntimeException {

    public InsufficientAmountException(String message) {
        super(message);
    }
}
