package com.backend.spring.test;

public class CheckingAccount extends BankAccount {

    private double dailyWithdrawalTotal;

    public CheckingAccount(String accountNumber, String accountHolderName) {
        super(accountNumber, accountHolderName);
        this.dailyWithdrawalTotal = 0.0;
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
        if (this.dailyWithdrawalTotal + amount > 2000) {
            throw new DailyWithdrawalLimitExceededException("Bạn không thể rút quá " + 2000 + " trong một ngày.");
        }
        this.balance -= amount;
        this.dailyWithdrawalTotal += amount;
    }
}
