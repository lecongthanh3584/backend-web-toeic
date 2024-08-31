package com.backend.spring.enums;

public enum ESection {
    LISTENING(0),
    READING(1),
    SPEAKING(2),
    WRITING(3);
    private final int value;
    ESection(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
