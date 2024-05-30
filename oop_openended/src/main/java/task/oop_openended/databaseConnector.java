package task.oop_openended;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnector {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/item";
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "5570"; // replace with your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
