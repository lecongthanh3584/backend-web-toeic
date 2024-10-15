package com.backend.spring.test;

public class DailyWithdrawalLimitExceededException extends RuntimeException {
    public DailyWithdrawalLimitExceededException(String message) {
        super(message);
    }
}
