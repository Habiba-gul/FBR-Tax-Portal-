package Backend;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryDAO {

    public List<PaymentHistory> getHistoryByUser(int userId) {
        List<PaymentHistory> history = new ArrayList<>();
        String sql = "SELECT id, payment_date, total_tax, details FROM payment_history WHERE user_id = ? ORDER BY payment_date DESC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                history.add(new PaymentHistory(
                        rs.getInt("id"),
                        rs.getTimestamp("payment_date").toLocalDateTime(),
                        rs.getDouble("total_tax"),
                        rs.getString("details")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public boolean insertPayment(int userId, double totalTax, String details) {
        String sql = "INSERT INTO payment_history (user_id, total_tax, details) VALUES (?, ?, ?)";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDouble(2, totalTax);
            ps.setString(3, details);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}