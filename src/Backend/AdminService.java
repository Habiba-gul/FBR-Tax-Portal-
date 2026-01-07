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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updatePenalty(int userId, double newPenalty) {
        String sql = "UPDATE taxpayer_profile SET penalty = ? WHERE user_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newPenalty);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int userId, String newStatus) {
        String sql = "UPDATE taxpayer_profile SET status = ? WHERE user_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendTaxReminder(int userId) {
        String title = "Tax Payment Reminder";
        String message = "Dear Taxpayer,\n\n" +
                         "This is a reminder that your tax payment is due soon. Failure to pay on time may result in penalties, default surcharge, and further legal action as per law.\n\n" +
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