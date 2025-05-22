package auth;


import database.DatabaseConnection;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4PreparedStatement;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager {
    DatabaseConnection connection;
    public AccountManager(DatabaseConnection newConnection){
        this.connection = newConnection;
    }
    public void register(String name, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.getConnection().prepareStatement(
                "SELECT * FROM auth_account WHERE NAME = ?"); //AND PASSWORD = ?
        preparedStatement.setString(1 , name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            throw new RuntimeException("User exists");
        }

    }
}
