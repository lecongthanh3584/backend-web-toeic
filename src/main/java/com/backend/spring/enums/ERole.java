package com.backend.spring.enums;

public enum ERole {
    LEARNER(1),
    ADMIN(2);

    private final int value;

    ERole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
