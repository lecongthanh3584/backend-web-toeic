package com.backend.spring.test;

import co.elastic.clients.elasticsearch.license.LicenseStatus;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(new SavingsAccount("001", "Messi"));
        bankAccountList.add(new CheckingAccount("002", "Ronaldo"));
        bankAccountList.add(new CreditAccount("003", "Faker"));

        while (true) {
            System.out.println("\nChọn thao tác:");
            System.out.println("1. Nạp tiền");
            System.out.println("2. Rút tiền");
            System.out.println("3. Kiểm tra số dư");
            System.out.println("4. Thoát");

            int choice = 0;
            try {
                System.out.print("Nhập lựa chọn của bạn: ");
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Vui lòng nhập một số nguyên.");
                scanner.next(); // Clear the invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Nhập số tài khoản: ");
                    String accNumber = scanner.next();
                    System.out.print("Nhập số tiền cần nạp: ");
                    try {
                        double depositAmount = scanner.nextDouble();
                        for (BankAccount account : bankAccountList) {
                            if (account.accountNumber.equals(accNumber)) {
                                account.deposit(depositAmount);
                                System.out.println("Nạp tiền thành công.");
                                break;
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Vui lòng nhập một số thực.");
                        scanner.next(); // Clear the invalid input
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Nhập số tài khoản: ");
                    accNumber = scanner.next();
                    System.out.print("Nhập số tiền cần rút: ");
                    try {
                        double withdrawAmount = scanner.nextDouble();
                        for (BankAccount account : bankAccountList) {
                            if (account.accountNumber.equals(accNumber)) {
                                account.withdraw(withdrawAmount);
                                System.out.println("Rút tiền thành công.");
                                break;
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Vui lòng nhập một số thực.");
                        scanner.next(); // Clear the invalid input
                    } catch (InsufficientBalanceException | DailyWithdrawalLimitExceededException | CreditLimitExceededException e) {
                        System.out.println(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Nhập số tài khoản: ");
                    accNumber = scanner.next();
                    for (BankAccount account : bankAccountList) {
                        if (account.accountNumber.equals(accNumber)) {
                            System.out.println("Số dư tài khoản: " + account.checkBalance());
                            break;
                        }
                    }
                    break;

                case 4:
                    System.out.println("Thoát chương trình.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }
}
