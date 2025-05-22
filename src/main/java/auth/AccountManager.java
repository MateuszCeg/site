package auth;


import at.favre.lib.crypto.bcrypt.BCrypt;
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
                "SELECT * FROM auth_account WHERE NAME = ?;"); //AND PASSWORD = ?
        preparedStatement.setString(1 , name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            throw new RuntimeException("User exists");
        }
        PreparedStatement preparedStatement1 = connection.getConnection().prepareStatement(
                "INSERT INTO auth_account(name,password) VALUES (?,?);");
        BCrypt.Hasher hasher = BCrypt.withDefaults();
        preparedStatement1.setString(1, name);
        preparedStatement1.setString(2, hasher.hashToString(12,password.toCharArray()));
        preparedStatement1.executeUpdate();
    }
    public boolean authenticate(String name, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.getConnection().prepareStatement(
                "SELECT PASSWORD FROM auth_account WHERE NAME = ?;");
        preparedStatement.setString(1 , name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new RuntimeException("User not exists");
        } else{
            String cryptedPassword = resultSet.getString("PASSWORD");
            BCrypt.Verifyer verifyer = BCrypt.verifyer();
            BCrypt.Hasher hasher = BCrypt.withDefaults();
            BCrypt.Result result = verifyer.verify(password.toCharArray(),hasher.hash(12, password.toCharArray()));
            return result.verified;
        }
    }
    public Account getAccount(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.getConnection().prepareStatement(
                "SELECT ID FROM auth_account WHERE NAME = ?;");
        preparedStatement.setString(1 , name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new RuntimeException("User not exists");
        } else{
            return new Account(resultSet.getInt("ID"), name);
        }
    }
}
