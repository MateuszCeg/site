package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void connect(String url) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:"+url);
        System.out.println("\nPołączono.\n");
    }
    public void disconnect() throws SQLException {
        connection.close();
        System.out.println("\nRozłączono.\n");
    }
}
