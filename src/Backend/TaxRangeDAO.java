package Backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaxRangeDAO {

    public List<TaxRange> getRangesByCategory(String category) {
        List<TaxRange> ranges = new ArrayList<>();
        String sql = "SELECT * FROM tax_ranges WHERE category = ? ORDER BY min_amount";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ranges.add(new TaxRange(
                        rs.getInt("id"),
                        rs.getString("category"),           // Use the exact category from DB
                        rs.getDouble("min_amount"),
                        rs.getDouble("max_amount"),
                        rs.getDouble("rate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ranges;
    }

    public boolean addRange(String category, double min, double max, double rate) {
        String sql = "INSERT INTO tax_ranges (category, min_amount, max_amount, rate) VALUES (?, ?, ?, ?)";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category);
            ps.setDouble(2, min);
            ps.setDouble(3, max);
            ps.setDouble(4, rate);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRange(int id, double min, double max, double rate) {
        String sql = "UPDATE tax_ranges SET min_amount = ?, max_amount = ?, rate = ? WHERE id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, min);
            ps.setDouble(2, max);
            ps.setDouble(3, rate);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            System.out.println("Updated " + rows + " row(s) for range ID: " + id);  // Debug help
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRange(int id) {
        String sql = "DELETE FROM tax_ranges WHERE id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}