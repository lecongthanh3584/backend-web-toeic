package com.backend.spring.enums;

public enum EExamType {
    MINI_TEST(0),
    FULL_TEST(1);

    private final Integer value;

    EExamType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
