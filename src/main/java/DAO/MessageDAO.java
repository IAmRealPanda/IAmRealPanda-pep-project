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

    // Get all Messages
    public List<Message> getAllMessages() {

        List<Message> messages = new ArrayList<>();

        try {
            String sql = "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // loop through to add to list
            while (resultSet.next()) {
                int message_id = resultSet.getInt("message_id");
                int posted_by = resultSet.getInt("posted_by");
                String message_text = resultSet.getString("message_text");
                long time_posted_epoch = resultSet.getLong("time_posted_epoch");
    
                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return messages; 
    }

    // get message by message ID
    public Message getMessageByID(int message_id) {
        Message message = null;

        try {
            String sql = "select * from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, message_id);
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // message found create message object
            if (resultSet.next()) {
                int posted_by = resultSet.getInt("posted_by");
                String message_text = resultSet.getString("message_text");
                long time_posted_epoch = resultSet.getLong("time_posted_epoch");
    
                message = new Message(message_id, posted_by, message_text, time_posted_epoch);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return message; 
    }

    // delete messaeg by id
    public Message deleteMessageByID (int messageId) {
        Message message = null;
        
        try {
            // select the message to delete
            String messageSelectSql = "select * from message where message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(messageSelectSql);
            selectStatement.setInt(1, messageId);
            ResultSet resultSet = selectStatement.executeQuery();
            
            // check if message exists
            if(resultSet.next()) {
                int posted_by = resultSet.getInt("posted_by");
                String message_text = resultSet.getString("message_text");
                long time_posted_epoch = resultSet.getLong("time_posted_epoch");

                // create message to send as response
                message = new Message(messageId, posted_by, message_text, time_posted_epoch);

                // delete message from db
                String deleteSql = "delete from message where message_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, messageId);
                deleteStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return message;
    }


}
