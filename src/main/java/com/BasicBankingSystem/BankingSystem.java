package com.BasicBankingSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import javax.xml.crypto.Data;

public class BankingSystem {
    public static void main(String[] args) {
        try (Connection conn = DatabaseManager.connect()) {
            if (conn != null) {
                DatabaseManager.initializeDatabase(conn);
                System.out.println("Welcome to the Banking System");
                showMenu(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showMenu(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Account Details");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    createAccount(conn); // Call the existing createAccount method
                    break;
                case 2:
                    depositMoney(conn, scanner);
                    break;
                case 3:
                    withdrawMoney(conn, scanner);
                    break;
                case 4:
                    viewAccountDetails(conn, scanner);
                    break;
                case 5:
                    running = false; // Exit the loop
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }


    private static void createAccount(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter initial deposit: ");
        double initialBalance = scanner.nextDouble();

        try {
            int accountId = DatabaseManager.createAccount(conn, name, initialBalance);
            System.out.println("Account created successfully! Your account ID is: " + accountId);
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    private static void depositMoney(Connection conn, Scanner scanner) {
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();

        try {
            Account account = DatabaseManager.getAccountDetails(conn, accountId);
        if (account != null) {
            System.out.println("You currently have: " + account.getBalance());
        } else {
            System.out.println("Account not found. \nReturning to main menu.");
            return;
        }
        } catch (SQLException e) {
           System.out.println("Error retrieving details " + e.getMessage());
        }
        
        
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        

        try {
            DatabaseManager.deposit(conn, accountId, amount);
            System.out.println("Deposit successful.");
        } catch (SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }

    private static void withdrawMoney(Connection conn, Scanner scanner) {
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();
        try {
            Account account = DatabaseManager.getAccountDetails(conn, accountId);
            if (account != null) {
                System.out.println("Your balance is " + account.getBalance());
            } else {
                System.out.println("Account not found. \n Returning to main menu.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account details: " + e.getMessage());
        }
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();

        try {
            DatabaseManager.withdraw(conn, accountId, amount);
            System.out.println("Withdrawal successful.");
        } catch (SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }

    private static void viewAccountDetails(Connection conn, Scanner scanner) {
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();

        try {
            Account account = DatabaseManager.getAccountDetails(conn, accountId);
            if (account != null) {
                System.out.println("Account ID: " + account.getId());
                System.out.println("Name: " + account.getName());
                System.out.println("Balance: " + account.getBalance());
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account details: " + e.getMessage());
        }
    }

}

