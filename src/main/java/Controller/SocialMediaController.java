package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.UserService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final UserService userService;

    public SocialMediaController(UserService userService) {
        this.userService = userService;
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
        app.put("/messages/{message_id}", this::updateMessage); //7
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
        Account addedUser = userService.registerUser(account);
        if(addedUser!=null){
            ctx.json(mapper.writeValueAsString(addedUser));
        }else{
            ctx.status(400);
        }
    }

    /**
     * Login for user look at Req2
     * @param ctx
     * @throws JsonProcessingException
     */
    private void login(Context ctx) throws JsonProcessingException {
    
    }

    /**
     * Create a new message object look at Req3
     * @param ctx
     * @throws JsonProcessingException
     */
    private void newMessage(Context ctx) throws JsonProcessingException {
    
    }
    
    // Req 4
    private void getAllMessages(Context ctx) {
    
    }

    // REq 5
    private void getMessageByID (Context ctx) throws JsonProcessingException {

    }

    // Req 6
    private void deleteMessageByID(Context ctx) {

    }
    // Req 7
    private void updateMessage(Context ctx) throws JsonProcessingException {

    }

    // Req 8
    private void getAllMessagesByUser(Context ctx) {

    }




}