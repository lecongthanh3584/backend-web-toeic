package com.backend.spring.enums;

public enum EUserStatus {
    INACTIVATE(0),
    ACTIVATE(1);

    private int value;

    EUserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
