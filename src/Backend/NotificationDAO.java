package Backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    // Add notification for a user
    public static void addNotification(int userId, String message) {
        String sql = "INSERT INTO notifications (user_id, title, message, type) VALUES (?, ?, ?, 'TAX_DUE')";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, "Tax Reminder");
            ps.setString(3, message);
            ps.executeUpdate();

            System.out.println("Notification added for user ID: " + userId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get notifications for user
    public static List<Notification> getUserNotifications(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getString("type"),
                        rs.getTimestamp("created_at"),
                        rs.getBoolean("is_read")
                );
                list.add(n);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
