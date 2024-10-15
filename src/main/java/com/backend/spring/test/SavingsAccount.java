package com.backend.spring.test;

public class SavingsAccount extends BankAccount {

    public SavingsAccount(String accountNumber, String accountHolderName) {
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
        if (this.balance - amount < 500) {
            throw new InsufficientBalanceException("Số dư không đủ để rút số tiền này. Bạn cần duy trì tối thiểu " + 500);
        }
        this.balance -= amount;
        if (this.balance < 500) {
            System.out.println("Bạn đã bị phạt vì số dư dưới mức tối thiểu.");
        }
    }
}
