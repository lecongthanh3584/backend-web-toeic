package com.backend.spring.exception;

public class AlreadyExistsException extends Exception {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
