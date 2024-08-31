package com.backend.spring.enums;

public enum EStatus {
    DISABLE(0),

    ENABLE(1);

    private Integer value;

    EStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
