package com.backend.spring.exception;

import lombok.Getter;

@Getter
public class RefreshTokenException extends Exception {
    private final int code;

    public RefreshTokenException(int code, String message) {
        super(message);
        this.code = code;
    }
}
