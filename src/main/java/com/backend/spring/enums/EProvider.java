package com.backend.spring.enums;

public enum EProvider {
    LOCAL(0),
    GOOGLE(1),
    FACEBOOK(2),
    GITHUB(3);

    private final int value;

    EProvider(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
