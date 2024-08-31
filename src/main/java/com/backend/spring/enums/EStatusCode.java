package com.backend.spring.enums;

public enum EStatusCode {
    GET_DATA_SUCCESS(1001),
    DATA_NOT_FOUND(1002),
    CREATE_SUCCESS(1003),
    CREATE_FAILED(1004),
    UPDATE_SUCCESS(1005),
    UPDATE_FAILED(1006),
    DELETE_SUCCESS(1007),
    DELETE_FAILED(1008),
    ACCOUNT_INACTIVATED(1010),
    ACCOUNT_LOCKED(1011),
    SIGNIN_SUCCESS(1012),
    SIGNIN_FAILED(1013);

    private final int value;

    EStatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
