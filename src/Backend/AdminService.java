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
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cnic"),
                        rs.getString("status"),
                        rs.getDouble("penalty")
                ));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return users;
    }

    public void updatePenalty(String cnic, double penalty) {
        String query = "UPDATE taxpayer_profile tp " +
                       "JOIN users u ON tp.user_id = u.id " +
                       "SET tp.penalty = ? " +
                       "WHERE u.cnic = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, penalty);
            pstmt.setString(2, cnic);
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

    public void updateStatus(String cnic, String status) {
        String query = "UPDATE taxpayer_profile tp " +
                       "JOIN users u ON tp.user_id = u.id " +
                       "SET tp.status = ? " +
                       "WHERE u.cnic = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setString(2, cnic);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Functional sendReminder: Inserts real DB entry with official message
    public void sendReminder(int userId, String userName) {
        String message = "Dear " + userName + ",\n\n" +
                         "This is an official reminder from the Federal Board of Revenue (FBR).\n\n" +
                         "You are required to pay your outstanding taxes at the earliest to comply with the Income Tax Ordinance, 2001.\n" +
                         "Failure to file/pay your tax return on time may result in penalties, default surcharge, and further legal action as per law.\n\n" +
                         "Please log in to the FBR Tax Portal immediately to calculate and pay your due taxes.\n" +
                         "Timely compliance will help avoid additional charges and ensure your status as an active taxpayer.\n\n" +
                         "For assistance, contact FBR Helpline at 111-772-772.\n\n" +
                         "Regards,\n" +
                         "Federal Board of Revenue (FBR)\n" +
                         "Government of Pakistan";

        // Call DAO to insert (real, not dummy)
        NotificationDAO.addNotification(userId, "Important Tax Due Reminder", message, "TAX_DUE");
    }
}