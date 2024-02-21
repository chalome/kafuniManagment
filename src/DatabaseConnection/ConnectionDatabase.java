package DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDatabase {

    private static Connection connection = null;
    private static String url = "jdbc:mysql://localhost/kafuniDb";
    private static String driver = "com.mysql.jdbc.Driver";
    private static String uname = "root";
    private static String password = "";

    public static Connection getConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, uname, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}
