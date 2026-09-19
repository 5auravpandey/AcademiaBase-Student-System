package com.academiabase.dao;

import com.academiabase.config.DatabaseConnection;
import com.academiabase.models.User;
import com.academiabase.models.User.Role;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final String INSERT_USER =
            "INSERT INTO Users (username, password_hash, role) VALUES (?, ?, ?)";
    private static final String SELECT_BY_USERNAME =
            "SELECT user_id, username, password_hash, role FROM Users WHERE username = ?";
    private static final String SELECT_BY_ID =
            "SELECT user_id, username, password_hash, role FROM Users WHERE user_id = ?";
    private static final String SELECT_ALL =
            "SELECT user_id, username, password_hash, role FROM Users";
    private static final String UPDATE_USER =
            "UPDATE Users SET username = ?, password_hash = ?, role = ? WHERE user_id = ?";
    private static final String DELETE_USER =
            "DELETE FROM Users WHERE user_id = ?";

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public User authenticateUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_USERNAME)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getString("password_hash").equals(hashedPassword)) {
                        return extractUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Authentication error: " + e.getMessage());
        }
        return null;
    }

    public int registerUser(String username, String password, Role role) {
        String hashedPassword = hashPassword(password);
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                pstmt.setString(3, role.name());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            int newId = keys.getInt(1);
                            System.out.println("[UserDAO] User registered with ID: " + newId);
                            return newId;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Registration error: " + e.getMessage());
        }
        return -1;
    }

    public User getUserById(int userId) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return extractUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error fetching user: " + e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) users.add(extractUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error fetching users: " + e.getMessage());
        }
        return users;
    }

    public boolean updateUser(User user) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_USER)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPasswordHash());
                pstmt.setString(3, user.getRole().name());
                pstmt.setInt(4, user.getUserId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error updating user: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteUser(int userId) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_USER)) {
                pstmt.setInt(1, userId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error deleting user: " + e.getMessage());
        }
        return false;
    }

    private User extractUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                Role.valueOf(rs.getString("role"))
        );
    }
}
