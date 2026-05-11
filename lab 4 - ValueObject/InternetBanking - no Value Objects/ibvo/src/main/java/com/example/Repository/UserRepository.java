package com.example.Repository;

import com.example.DatabaseConfig;
import com.example.User;
import java.sql.*;

public class UserRepository {
    public User findById(String id) {
        String sql = "SELECT id, name, email, username, password FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                return new User(id, name, email, username, password);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, name, email, username, password FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                return new User(id, name, email, username, password);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
