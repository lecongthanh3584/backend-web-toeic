package com.backend.spring.test;

public abstract class BankAccount {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;

    public BankAccount(String accountNumber, String accountHolderName) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = 0.0; // Khởi tạo số dư bằng 0
    }

    public abstract void deposit(double amount) throws IllegalArgumentException;

    public abstract void withdraw(double amount) throws RuntimeException;

    public double checkBalance() {
        return this.balance;
    }

}
