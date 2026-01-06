package Backend;

import java.sql.*;
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

    public static boolean updateUser(UserInfo user) {
        String sql = "UPDATE users SET name=?, dob=?, gender=?, address=?, email=?, phone=? WHERE cnic=?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setDate(2, user.getDob() != null ? Date.valueOf(user.getDob()) : null);
            ps.setString(3, user.getGender());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getCnic());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<UserInfo> getUnpaidUsers() {
        List<UserInfo> list = new ArrayList<>();
        String sql = "SELECT u.*, tp.status FROM users u JOIN taxpayer_profile tp ON u.id = tp.user_id WHERE tp.status='Unpaid'";
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
                user.setTaxPaid("Paid".equalsIgnoreCase(rs.getString("status")));
                list.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
