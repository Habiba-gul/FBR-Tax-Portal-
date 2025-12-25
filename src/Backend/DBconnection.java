package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    
    private static final String URL = "jdbc:mysql://localhost:3306/fbr_tax_portal";
    private static final String USER = "root"; 
    private static final String PASSWORD = "Habiba_19"; 
    //private static final String PASSWORD = "affankhan11"; 

    public static Connection getConnection() throws SQLException {
        try {
          
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database Driver not found.", e);
        }
    }
}