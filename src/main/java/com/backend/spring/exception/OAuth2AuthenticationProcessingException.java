package com.backend.spring.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuth2AuthenticationProcessingException extends RuntimeException {

    private final int code;

    public OAuth2AuthenticationProcessingException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
