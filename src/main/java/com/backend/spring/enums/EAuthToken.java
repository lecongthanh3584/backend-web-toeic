package com.backend.spring.enums;

public enum EAuthToken {
    VERIFY_ACCOUNT(0),
    RESET_PASSWORD(1);

    private final int value;
    EAuthToken(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
