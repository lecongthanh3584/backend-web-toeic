package com.backend.spring.enums;

public enum EGender {
    FEMALE(0),
    MALE(1);

    private int value;

    EGender(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
