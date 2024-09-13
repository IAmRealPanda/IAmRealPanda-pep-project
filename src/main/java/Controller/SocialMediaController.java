package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import static org.mockito.ArgumentMatchers.matches;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.*;
import Model.*;
import Service.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final UserService userService;
    private final MessageService messageService;
    // no-arg constructor

    public SocialMediaController() {
        // Initialize userService here (either a default or mock implementation)
        this.userService = new UserService(new UserDAO());
        this.messageService = new MessageService(new MessageDAO(), new UserDAO());
    }

    public SocialMediaController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // req 1 new user
        app.post("/register", this::userRegistration); 

        // req 2 user login
        app.post("/login", this::login); 

        // messages related
        app.post("/messages", this::newMessage); //3
        app.get("/messages", this::getAllMessages); //4
        app.get("/messages/{message_id}", this::getMessageByID); //5
        app.delete("/messages/{message_id}", this::deleteMessageByID); //6
        app.patch("/messages/{message_id}", this::updateMessage); //7
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUser); //8
        return app;
    }


    /**
     * Create a new user Registration look at Req1
     * @param ctx
     * @throws JsonProcessingException
     */
    private void userRegistration(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        try {
            Account addedUser = userService.registerUser(account);
            ctx.json(mapper.writeValueAsString(addedUser)).status(200);
        } catch (IllegalArgumentException e) {
        // input validation fails return 400
            ctx.status(400);
        } catch (RuntimeException e) {
        // any other errors  return 500
            ctx.status(500);
        }
    }

    /**
     * Login for user look at Req2
     * @param ctx
     * @throws JsonProcessingException
     */
    private void login(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account credentials = mapper.readValue(ctx.body(), Account.class);
        
        try {
            Account account = userService.login(credentials.getUsername(), credentials.getPassword());
            if (account != null) {
                // Login successful, return the Account details
                ctx.json(mapper.writeValueAsString(account)).status(200);
            } else {
                // Login failed, unauthorized
                ctx.status(401);
            }
        } catch (IllegalArgumentException e) {
            //  invalid input 
            ctx.status(401);
        } catch (RuntimeException e) {
            // nexpected errors
            ctx.status(500);
        }
    }

    /**
     * Create a new message object look at Req3
     * @param ctx
     * @throws JsonProcessingException
     */
    private void newMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            //deserialize
            Message message = mapper.readValue(ctx.body(), Message.class);

            //call service
            Message createdMessage = messageService.newMessage(message);
            if(createdMessage != null) {
                ctx.json(mapper.writeValueAsString(createdMessage));
            } else {
                ctx.status(400);
            }
        } catch (IllegalArgumentException e) {
            //  invalid input 
            ctx.status(400);
        } catch (RuntimeException e) {
            // nexpected errors
            ctx.status(500);
        }
    }
    /*
     * Req 4 get all messages - return empty list if no messages found 
     */
    private void getAllMessages(Context ctx) throws JsonProcessingException  {
        // call service
        List<Message> allMessages = messageService.getAllMessages();
        
        ObjectMapper mapper = new ObjectMapper();
        ctx.json(mapper.writeValueAsString(allMessages));
    }

    /*
     * Req 5 get a message by message ID - empty message if no message found
     */
    private void getMessageByID (Context ctx) throws JsonProcessingException {
        // parse the id
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        //service
        Message message = messageService.getMessageByID(id);
        ObjectMapper mapper = new ObjectMapper();
        if (message != null) {
            ctx.json(mapper.writeValueAsString(message));
        } else {
            ctx.status(200).json(""); 
        }
    }

    /*
     * Req 6 Delete message by id. send response as deleted message. else send empty response
     */
    private void deleteMessageByID(Context ctx) throws JsonProcessingException {
        // parse the id
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        // call service
        Message deletedMessage = messageService.deleteMessageByID(id);
        ObjectMapper mapper = new ObjectMapper();

        if (deletedMessage != null) {
            ctx.json(mapper.writeValueAsString(deletedMessage));
        } else {
            ctx.status(200).json("");
        }
    }
    /*
     * req 7 update the message and return the new message
     */
    private void updateMessage(Context ctx) throws JsonProcessingException {
    
        try {
            // parse the id
            int id = Integer.parseInt(ctx.pathParam("message_id"));

            // extract the new updated message
            ObjectMapper mapper = new ObjectMapper();
            Message original = mapper.readValue(ctx.body(), Message.class);
            String newMsgTxt = original.getMessage_text();

            // call service
            Message updtMsg = messageService.updateMessageByID(id, newMsgTxt);
            if (updtMsg != null) {
                ctx.json(mapper.writeValueAsString(updtMsg));
            } else {
                ctx.status(400);
            }
        } catch (IllegalArgumentException e) {
            //  invalid input 
            ctx.status(400);
        } catch (RuntimeException e) {
            // nexpected errors
            ctx.status(500);
        }
    }

    // Req 8
    private void getAllMessagesByUser(Context ctx) {

    }




}