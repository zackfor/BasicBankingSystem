package com.BasicBankingSystem;

public class Account {
    private int id;         // Account ID
    private String name;    // Account holder's name
    private double balance;  // Account balance

    // Constructor to initialize the account with ID, name, and balance
    public Account(int id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    // Getter for account ID
    public int getId() {
        return id;
    }

    // Getter for account holder's name
    public String getName() {
        return name;
    }

    // Getter for account balance
    public double getBalance() {
        return balance;
    }

    // Optional setters (if you want to allow changes after creation)
    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

