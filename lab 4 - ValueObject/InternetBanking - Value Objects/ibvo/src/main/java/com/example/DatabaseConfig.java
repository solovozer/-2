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
                "balance_amount INTEGER, " +
                "balance_currency TEXT)";

        String transTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "from_id TEXT, " +
                "to_id TEXT, " +
                "amount INTEGER, " +
                "currency TEXT, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(userTable);
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
            pstmt.setLong(3, money.getAmount()); 
            pstmt.setString(4, money.getCurrency());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}