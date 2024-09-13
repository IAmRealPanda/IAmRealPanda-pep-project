package Service;

import java.sql.SQLException;

import DAO.UserDAO;
import Model.Account;

public class UserService {

    private UserDAO userDAO;
    /**
     * no-args constructor for creating a new AuthorService with a new AuthorDAO.
     * There is no need to change this constructor.
     */
    public UserService(){
        userDAO = new UserDAO();
    }
    /**
     * Constructor for a AuthorService when a AuthorDAO is provided.
     * This is used for when a mock AuthorDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of AuthorService independently of AuthorDAO.
     * There is no need to modify this constructor.
     * @param authorDAO
     */
    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    // register user
    public Account registerUser (Account newUser) {
        //validate user input
        //blank username
        if (newUser.getUsername() == null || newUser.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        // 4 char password
        if (newUser.getPassword() == null || newUser.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long");
        }

         // Check if the username already exists
        if (userDAO.usernameExists(newUser.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        try {
            // Attempt to register the user in the database
            return userDAO.registerUser(newUser);
        } catch (SQLException e) {
            throw new RuntimeException("Error registering user: " + e.getMessage(), e);
        }
    }

    public Account login(String username, String password) {
        //  validation
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username and password cannot be blank");
        }

        try {
            
            Account account = userDAO.validateLogin(username, password);
            if (account != null) {
                return account;  // Login successful
            } else {
                return null;  // Login failed
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during login: " + e.getMessage(), e);
        }
    }

}
