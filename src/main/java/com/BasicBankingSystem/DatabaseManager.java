package com.BasicBankingSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:banking.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void initializeDatabase(Connection conn) {
        try {
            // Load schema.sql file from resources
            InputStream inputStream = DatabaseManager.class.getResourceAsStream("/schema.sql");
            if (inputStream != null) {
                String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                }
                System.out.println("Database initialized successfully.");
            } else {
                System.out.println("schema.sql not found.");
            }
        } catch (Exception e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public static int createAccount(Connection conn, String name, double initialBalance) throws SQLException {
        String sql = "INSERT INTO accounts (name, balance) VALUES ('" + name + "', " + initialBalance + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            return getLastInsertedId(conn);
        }
    }

    private static int getLastInsertedId(Connection conn) throws SQLException {
        String lastIdQuery = "SELECT last_insert_rowid()";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(lastIdQuery)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Creating account failed, no ID obtained.");
            }
        }
    }

    public static void deposit(Connection conn, int accountId, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + " + amount + " WHERE id = " + accountId;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public static void withdraw(Connection conn, int accountId, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance - " + amount + " WHERE id = " + accountId;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public static Account getAccountDetails(Connection conn, int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE id = " + accountId;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String name = rs.getString("name");
                double balance = rs.getDouble("balance");
                return new Account(accountId, name, balance);
            }
        }
        return null; // Return null if account not found
    }

}

