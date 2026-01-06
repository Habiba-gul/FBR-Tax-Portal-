package Backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminService {

    // The connection details are now managed in the DBconnection class

    public ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        
        // SQL query to get user details from the database
        String query = "SELECT u.cnic, u.name, tp.status, tp.penalty, tp.base_tax " +
                       "FROM users u " +
                       "JOIN taxpayer_profile tp ON u.id = tp.user_id " +
                       "WHERE u.role = 'USER'";
        
        // Use the DBconnection class to get the connection
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getString("cnic"),
                    rs.getString("name"),
                    rs.getString("status"),
                    rs.getDouble("penalty")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return users;
    }

    public ObservableList<User> getDefaulterList() {
        ObservableList<User> users = FXCollections.observableArrayList();
        
        String query = "SELECT u.cnic, u.name, tp.status, tp.penalty, tp.base_tax " +
                       "FROM users u " +
                       "JOIN taxpayer_profile tp ON u.id = tp.user_id " +
                       "WHERE u.role = 'USER' AND tp.status = 'Unpaid'";
        
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getString("cnic"),
                    rs.getString("name"),
                    rs.getString("status"),
                    rs.getDouble("penalty")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching defaulter list: " + e.getMessage());
            e.printStackTrace();
        }
        
        return users;
    }

    public void applyPenalty(String cnic) {
        String query = "UPDATE taxpayer_profile tp " +
                       "JOIN users u ON tp.user_id = u.id " +
                       "SET tp.penalty = tp.base_tax * 0.1, tp.status = 'Unpaid' " +
                       "WHERE u.cnic = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, cnic);
            pstmt.executeUpdate();
            System.out.println("Penalty applied for CNIC: " + cnic);
        } catch (SQLException e) {
            System.err.println("Error applying penalty: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void overridePenalty(String cnic) {
        String query = "UPDATE taxpayer_profile tp " +
                       "JOIN users u ON tp.user_id = u.id " +
                       "SET tp.penalty = 0, tp.status = 'Paid' " +
                       "WHERE u.cnic = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, cnic);
            pstmt.executeUpdate();
            System.out.println("Penalty overridden for CNIC: " + cnic);
        } catch (SQLException e) {
            System.err.println("Error overriding penalty: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void suspendUser(String cnic) {
        String query = "UPDATE taxpayer_profile tp " +
                       "JOIN users u ON tp.user_id = u.id " +
                       "SET tp.status = 'Suspended' " +
                       "WHERE u.cnic = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, cnic);
            pstmt.executeUpdate();
            System.out.println("User suspended for CNIC: " + cnic);
        } catch (SQLException e) {
            System.err.println("Error suspending user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean testConnection() {
        try (Connection conn = DBconnection.getConnection()) {
            // A simple query to check if the connection is valid and the database is accessible
            try (Statement stmt = conn.createStatement()) {
                stmt.executeQuery("SELECT 1");
            }
            System.out.println("Database connection successful!");
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}