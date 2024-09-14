package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
import DAO.UserDAO;
import Model.Message;

public class MessageService {

    private MessageDAO msgDAO;
    private UserDAO userDAO;
 
    // no arg constructor
    public MessageService(){
        msgDAO = new MessageDAO();
        userDAO = new UserDAO();
    }
    // arg constructor
    public MessageService(MessageDAO messageDAO, UserDAO userDAO){
        this.msgDAO = messageDAO;
        this.userDAO = userDAO;
    }

    public Message newMessage(Message msg) {
        //validate user input
        // blank message
        if(msg.getMessage_text() == null || msg.getMessage_text().isBlank()) {
            throw new IllegalArgumentException("Message cannot be blank");
        }
        // longer that 255 char
        if(msg.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message must be under 255 characters");  
        }

        // user exists
        if(!userDAO.userExists(msg.getPosted_by())) {
            throw new IllegalArgumentException("User does not Exist");  
        }
        // pass validation now send to dao
        try {
            return msgDAO.newMessage(msg);
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating message: " + e.getMessage());
        }
    }

    // get all messages
    public List<Message> getAllMessages() {
        //call dao
        return msgDAO.getAllMessages();
    }

    // get message by message ID
    public Message getMessageByID (int id) {
        //call dao
        return msgDAO.getMessageByID(id);
    }

    // delete mess by message ID
    public Message deleteMessageByID (int id) {
        // call dao
        return msgDAO.deleteMessageByID(id);
    }

    // update message by ID
    public Message updateMessageByID (int id, String newMessage) {

        // validation
        // blank message and under 255 char
        if(newMessage == null || newMessage.isBlank() || newMessage.length() > 255) {
            throw new IllegalArgumentException("Message cannot be blank and under 255 characters");
        }
        // call dao
        return msgDAO.updateMessageByID(id,newMessage);
    }

    // get messages by account id
    public List<Message> getMessagesByAccountID (int id) {
        //call dao
        return msgDAO.getMessageByAccountID(id);
    }
}
