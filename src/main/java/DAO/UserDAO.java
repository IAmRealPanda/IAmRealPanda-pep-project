package DAO;
import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    Connection connection = ConnectionUtil.getConnection();

    // Register User
    public Account registerUser(Account account) {
        try {
            // sql statement
            String sql = "insert into account (username, password) values (?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //add params
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            //execute
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            //return the new account object
            if(pkeyResultSet.next()){
                int generated_author_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_author_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
      
        return null;
    }

    // username Exists or not
    public boolean usernameExists(String username) {
        Connection connection = ConnectionUtil.getConnection();
        boolean exists = false;
        
        try {
            // SQL query 
            String sql = "select count(*) FROM account where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // fill params
            preparedStatement.setString(1, username);
            
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Check the result
            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;  // If count > 0, username exists
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        
        return exists; // Return whether the username exists
    }
    

}
