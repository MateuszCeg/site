import database.DatabaseConnection;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.connect("shop.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            databaseConnection.disconnect();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}