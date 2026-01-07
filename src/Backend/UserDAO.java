package Backend;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static UserInfo getUserByCNIC(String cnic) {
        String sql = "SELECT * FROM users WHERE cnic = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cnic);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserInfo user = new UserInfo();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setCnic(rs.getString("cnic"));
                user.setDob(rs.getDate("dob") != null ? rs.getDate("dob").toLocalDate() : null);
                user.setGender(rs.getString("gender"));
                user.setAddress(rs.getString("address"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserInfo> getAllUsers() {
        List<UserInfo> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'USER'";
        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UserInfo user = new UserInfo();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setCnic(rs.getString("cnic"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                list.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean updateUser(UserInfo user) {
        String sql = "UPDATE users SET name = ?, dob = ?, gender = ?, address = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            if (user.getDob() != null) {
                ps.setDate(2, java.sql.Date.valueOf(user.getDob()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            ps.setString(3, user.getGender());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPhone());
            ps.setInt(7, user.getId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user profile: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateTaxStatus(int userId, String status) {
        String sql = "UPDATE taxpayer_profile SET status = ? WHERE user_id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updatePaymentDate(int userId, Date date) {
        String sql = "UPDATE taxpayer_profile SET payment_date = ? WHERE user_id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, date);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static double getPenalty(int userId) {
        String sql = "SELECT penalty FROM taxpayer_profile WHERE user_id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("penalty");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // NEW METHOD: Update penalty amount in taxpayer_profile
    public static boolean updatePenalty(int userId, double amount) {
        String sql = "UPDATE taxpayer_profile SET penalty = ? WHERE user_id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}