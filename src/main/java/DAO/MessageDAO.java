package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MessageDAO {

    Connection connection = ConnectionUtil.getConnection();

    //Create New Message
    public Message newMessage(Message message) throws SQLException {
        try {
            // sql statement
            String sql = "insert into message(posted_by,message_text,time_posted_epoch) values (?,?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //add params
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.message_text);
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            //execute
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            //return the new account object
            if(pkeyResultSet.next()){
                int generated_mesg_id = (int) pkeyResultSet.getLong(1); // grab the id
                return new Message(generated_mesg_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            throw new SQLException("Error inserting message: " + e.getMessage(), e);
        }
      
        return null;

    }

    
}
