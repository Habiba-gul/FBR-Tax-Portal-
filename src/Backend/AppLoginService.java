package Backend;

import java.sql.*;

public class AppLoginService {

    public String validateLogin(String cnic, String password) {
        String sql = "SELECT role FROM users WHERE cnic = ? AND password = ?";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cnic);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("role");  // "USER" ya "ADMIN" return karega
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // Invalid credentials (null return karo taake LoginController "INVALID" na samjhe)
    }
}