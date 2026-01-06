package Backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class AdminService {

    public ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT u.id, u.cnic, u.name, tp.status, tp.penalty " +
                       "FROM users u " +
                       "JOIN taxpayer_profile tp ON u.id = tp.user_id " +
                       "WHERE u.role = 'USER'";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),      // FETCH ID
                        rs.getString("name"),
                        rs.getString("cnic"),
                        rs.getString("status"),
                        rs.getDouble("penalty")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public ObservableList<User> getDefaulterList() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT u.id, u.cnic, u.name, tp.status, tp.penalty " +
                       "FROM users u " +
                       "JOIN taxpayer_profile tp ON u.id = tp.user_id " +
                       "WHERE u.role = 'USER' AND tp.status = 'Unpaid'";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),      // FETCH ID
                        rs.getString("name"),
                        rs.getString("cnic"),
                        rs.getString("status"),
                        rs.getDouble("penalty")
                ));
            }
        } catch (SQLException e) {
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
        } catch (SQLException e) { e.printStackTrace(); }
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
        } catch (SQLException e) { e.printStackTrace(); }
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
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // NEW: Send Reminder
    public void sendReminder(int userId) {
        String msg = "Your tax payment is due. Please pay as soon as possible.";
        Backend.NotificationDAO.addNotification(userId, msg);
    }
}
