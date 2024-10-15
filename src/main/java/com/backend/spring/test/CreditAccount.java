package com.backend.spring.test;

public class CreditAccount extends BankAccount {

    public CreditAccount(String accountNumber, String accountHolderName) {
        super(accountNumber, accountHolderName);
    }

    @Override
    public void deposit(double amount) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Số tiền nạp không hợp lệ.");
        }

        this.balance += amount;
    }

    @Override
    public void withdraw(double amount) throws RuntimeException {
        if (amount < 0) {
            throw new IllegalArgumentException("Số tiền rút không hợp lệ.");
        }
        if (this.balance - amount < 1000) {
            throw new CreditLimitExceededException("Bạn không thể rút quá hạn mức tín dụng (" + 1000 + ").");
        }
        this.balance -= amount;
    }
}
