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

        String convTable = "CREATE TABLE IF NOT EXISTS conversions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "amount INTEGER, " +
                "from_currency TEXT, " +
                "to_currency TEXT, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(userTable);
            stmt.execute(accountTable);
            stmt.execute(transTable);
            stmt.execute(convTable);
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

    public void saveConversion(String userId, Money amount, String fromCurrency, String toCurrency) {
        String sql = "INSERT INTO conversions(user_id, amount, from_currency, to_currency) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setBigDecimal(2, amount.getAmount());
            pstmt.setString(3, fromCurrency);
            pstmt.setString(4, toCurrency);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public java.util.List<TransactionRecord> getTransactionsForAccount(Account account) {
        String sql = "SELECT t.id, t.from_id, t.to_id, t.amount, t.currency, t.timestamp, " +
                    "u1.username as from_username, u2.username as to_username " +
                    "FROM transactions t " +
                    "LEFT JOIN accounts a1 ON t.from_id = a1.id " +
                    "LEFT JOIN users u1 ON a1.user_id = u1.id " +
                    "LEFT JOIN accounts a2 ON t.to_id = a2.id " +
                    "LEFT JOIN users u2 ON a2.user_id = u2.id " +
                    "WHERE t.from_id = ? OR t.to_id = ? ORDER BY t.timestamp DESC";
        
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
                    rs.getBigDecimal("amount"),
                    rs.getString("currency"),
                    rs.getString("timestamp"),
                    rs.getString("from_username"),
                    rs.getString("to_username")
                );
                transactions.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return transactions;
    }

    public java.util.List<TransactionRecord> getRecentTransactionsForAccount(String accountId, int limit) {
        String sql = "SELECT t.id, t.from_id, t.to_id, t.amount, t.currency, t.timestamp, " +
                    "u1.username as from_username, u2.username as to_username " +
                    "FROM transactions t " +
                    "LEFT JOIN accounts a1 ON t.from_id = a1.id " +
                    "LEFT JOIN users u1 ON a1.user_id = u1.id " +
                    "LEFT JOIN accounts a2 ON t.to_id = a2.id " +
                    "LEFT JOIN users u2 ON a2.user_id = u2.id " +
                    "WHERE t.from_id = ? OR t.to_id = ? ORDER BY t.timestamp DESC LIMIT ?";
        
        java.util.List<TransactionRecord> transactions = new java.util.ArrayList<>();
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountId);
            pstmt.setString(2, accountId);
            pstmt.setInt(3, limit);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TransactionRecord record = new TransactionRecord(
                    rs.getInt("id"),
                    rs.getString("from_id"),
                    rs.getString("to_id"),
                    rs.getBigDecimal("amount"),
                    rs.getString("currency"),
                    rs.getString("timestamp"),
                    rs.getString("from_username"),
                    rs.getString("to_username")
                );
                transactions.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return transactions;
    }

    public java.util.List<TransactionRecord> getAllTransactionHistory() {
        String sql = "SELECT t.id, t.from_id, t.to_id, t.amount, t.currency, t.timestamp, " +
                    "u1.username as from_username, u2.username as to_username " +
                    "FROM transactions t " +
                    "LEFT JOIN accounts a1 ON t.from_id = a1.id " +
                    "LEFT JOIN users u1 ON a1.user_id = u1.id " +
                    "LEFT JOIN accounts a2 ON t.to_id = a2.id " +
                    "LEFT JOIN users u2 ON a2.user_id = u2.id " +
                    "ORDER BY t.timestamp DESC";
        
        java.util.List<TransactionRecord> transactions = new java.util.ArrayList<>();
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TransactionRecord record = new TransactionRecord(
                    rs.getInt("id"),
                    rs.getString("from_id"),
                    rs.getString("to_id"),
                    rs.getBigDecimal("amount"),
                    rs.getString("currency"),
                    rs.getString("timestamp"),
                    rs.getString("from_username"),
                    rs.getString("to_username")
                );
                transactions.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    public java.util.List<ConversionRecord> getConversionHistory(String userId) { 
        String sql = "SELECT id, user_id, amount, from_currency, to_currency, timestamp " +
                    "FROM conversions WHERE user_id = ? ORDER BY timestamp DESC";
        
        java.util.List<ConversionRecord> conversions = new java.util.ArrayList<>();
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ConversionRecord record = new ConversionRecord(
                    rs.getInt("id"),
                    rs.getString("user_id"),
                    rs.getBigDecimal("amount"),
                    rs.getString("from_currency"),
                    rs.getString("to_currency"),
                    rs.getString("timestamp")
                );
                conversions.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return conversions;
    }

    public static class TransactionRecord {
        public final int id;
        public final String fromId;
        public final String toId;
        public final java.math.BigDecimal amount;
        public final String currency;
        public final String timestamp;
        public final String fromUsername;
        public final String toUsername;
        
        public TransactionRecord(int id, String fromId, String toId, java.math.BigDecimal amount, String currency, String timestamp) {
            this(id, fromId, toId, amount, currency, timestamp, null, null);
        }
        
        public TransactionRecord(int id, String fromId, String toId, java.math.BigDecimal amount, String currency, String timestamp, String fromUsername, String toUsername) {
            this.id = id;
            this.fromId = fromId;
            this.toId = toId;
            this.amount = amount;
            this.currency = currency;
            this.timestamp = timestamp;
            this.fromUsername = fromUsername;
            this.toUsername = toUsername;
        }
    }

    public static class ConversionRecord {
        public final int id;
        public final String userId;
        public final java.math.BigDecimal amount;
        public final String fromCurrency;
        public final String toCurrency;
        public final String timestamp;

        public ConversionRecord(int id, String userId, java.math.BigDecimal amount, String fromCurrency, String toCurrency, String timestamp) {
            this.id = id;
            this.userId = userId;
            this.amount = amount;
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.timestamp = timestamp;
        }
    }
}