package com.example;

import java.sql.*;

public class DatabaseConfig {
    private static final String URL = "jdbc:sqlite:bank.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id TEXT PRIMARY KEY, " +
                "name TEXT, " +
                "email TEXT, " +
                "username TEXT, " +
                "password TEXT)";

        String accountTable = "CREATE TABLE IF NOT EXISTS accounts (" +
                "id TEXT PRIMARY KEY, " +
                "user_id TEXT, " +
                "balance_amount INTEGER, " +
                "balance_currency TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES users(id))";

        String transTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "from_id TEXT, " +
                "to_id TEXT, " +
                "amount INTEGER, " +
                "currency TEXT, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(userTable);
            stmt.execute(accountTable);
            stmt.execute(transTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveTransaction(String fromId, String toId, Money money) {
        String sql = "INSERT INTO transactions(from_id, to_id, amount, currency) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConfig.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fromId);
            pstmt.setString(2, toId);
            pstmt.setBigDecimal(3, money.getAmount()); 
            pstmt.setString(4, money.getCurrency());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public java.util.List<TransactionRecord> getTransactionsForAccount(Account account) {
        String sql = "SELECT id, from_id, to_id, amount, currency, timestamp FROM transactions " +
                    "WHERE from_id = ? OR to_id = ? ORDER BY timestamp DESC";
        
        java.util.List<TransactionRecord> transactions = new java.util.ArrayList<>();
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, account.getId());
            pstmt.setString(2, account.getId());
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TransactionRecord record = new TransactionRecord(
                    rs.getInt("id"),
                    rs.getString("from_id"),
                    rs.getString("to_id"),
                    rs.getLong("amount"),
                    rs.getString("currency"),
                    rs.getString("timestamp")
                );
                transactions.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return transactions;
    }

    public java.util.List<TransactionRecord> getAllTransactionHistory() {
        String sql = "SELECT id, from_id, to_id, amount, currency, timestamp FROM transactions ORDER BY timestamp DESC";
        
        java.util.List<TransactionRecord> transactions = new java.util.ArrayList<>();
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TransactionRecord record = new TransactionRecord(
                    rs.getInt("id"),
                    rs.getString("from_id"),
                    rs.getString("to_id"),
                    rs.getLong("amount"),
                    rs.getString("currency"),
                    rs.getString("timestamp")
                );
                transactions.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    public static class TransactionRecord {
        public final int id;
        public final String fromId;
        public final String toId;
        public final long amount;
        public final String currency;
        public final String timestamp;
        
        public TransactionRecord(int id, String fromId, String toId, long amount, String currency, String timestamp) {
            this.id = id;
            this.fromId = fromId;
            this.toId = toId;
            this.amount = amount;
            this.currency = currency;
            this.timestamp = timestamp;
        }
    }
}