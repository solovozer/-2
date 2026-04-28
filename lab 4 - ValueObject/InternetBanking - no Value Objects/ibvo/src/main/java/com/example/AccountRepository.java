package com.example;

import java.sql.*;

public class AccountRepository {
    
    public Account findById(String id) {
        String sql = "SELECT id, name, balance_amount, balance_currency FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String name = rs.getString("name");
                long amount = rs.getLong("balance_amount");
                String currency = rs.getString("balance_currency");
                Money balance = new Money(amount, currency);
                return new Account(id, name, balance);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public void updateBalance(Account account) {
        String sql = "UPDATE users SET balance_amount = ?, balance_currency = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, account.getBalance().getAmount());
            pstmt.setString(2, account.getBalance().getCurrency());
            pstmt.setString(3, account.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void logTransaction(String fromId, String toId, Money amount) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.saveTransaction(fromId, toId, amount);
    }
}
