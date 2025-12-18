package Backend;

import java.sql.*;

public class UserDAO {

    // Fetch full user details by CNIC
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

                Date dobSql = rs.getDate("dob");
                user.setDob(dobSql != null ? dobSql.toLocalDate() : null);

                user.setGender(rs.getString("gender"));
                user.setAddress(rs.getString("address"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update existing user profile
    public static boolean updateUser(UserInfo user) {
        String sql = "UPDATE users SET name = ?, dob = ?, gender = ?, address = ?, email = ?, phone = ? WHERE cnic = ?";
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Register new user (ye method ab fully working hai)
    public static boolean registerUser(String name, String cnic, String email, String phone, String password) {
        String sql = "INSERT INTO users (name, cnic, email, phone, password, role) VALUES (?, ?, ?, ?, ?, 'USER')";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, cnic);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, password);  // Testing ke liye plain text (baad mein hash kar sakte ho)

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;  // Success → true

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}