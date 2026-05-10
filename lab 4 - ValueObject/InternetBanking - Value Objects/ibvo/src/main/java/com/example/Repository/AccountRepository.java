package com.example.Repository;

import java.math.BigDecimal;
import java.sql.*;

import com.example.DatabaseConfig;
import com.example.Money;

import com.example.Record.AccountRecord;

public class AccountRepository {
    
    public AccountRecord findById(String id) {
        String sql = "SELECT id, name, balance_amount, balance_currency, user_id FROM accounts WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal amount = rs.getBigDecimal("balance_amount");
                String currency = rs.getString("balance_currency");
                String userId = rs.getString("user_id");
                return new AccountRecord(id, userId, amount, currency);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public java.util.List<AccountRecord> getAllAccountsFromUser(String userId) {
        String sql = "SELECT id, name, balance_amount, balance_currency, user_id FROM accounts WHERE user_id = ?";
        java.util.List<AccountRecord> accounts = new java.util.ArrayList<>();

        try (Connection conn = DatabaseConfig.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userId);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    String id = rs.getString("id");
                    BigDecimal amount = rs.getBigDecimal("balance_amount");
                    String currency = rs.getString("balance_currency");
                    accounts.add(new AccountRecord(id, userId, amount, currency));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        return accounts;
    }

    public void updateBalance(AccountRecord account) {
        String sql = "UPDATE accounts SET balance_amount = ?, balance_currency = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBigDecimal(1, account.balanceAmount);
            pstmt.setString(2, account.balanceCurrency);
            pstmt.setString(3, account.id);
            
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
