package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    private static final String URL = "jdbc:mysql://localhost:3306/fbr_tax_portal";
    private static final String USER = "root";
  //  private static final String PASSWORD = "affankhan11";
    private static final String PASSWORD = "Habiba_19";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(true);  // Keep this, but we'll use explicit commit in DAO
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database Driver not found.", e);
        }
    }

    // Helper for explicit commit (call after batch updates)
    public static void commit(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper for rollback on error
    public static void rollback(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}