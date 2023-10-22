import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class UserManager {
    private static UserManager instance;
    private static final String USER_DATA_FILE = "users.dat";
    private HashMap<String, User> users = new HashMap<>();
    private HashMap<String, Boolean> loggedInUsers = new HashMap<>();
    
    // Reference to the SessionManager instance
    private SessionManager sessionManager = SessionManager.getInstance();

    private UserManager() {
    	loadUsers(); // Load users from the file when initializing the UserManager
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // Creating a user, saving the updated user list to the file
    public boolean createUser(String username, String password, String firstName, String lastName) {
        if (users.containsKey(username)) {
            return false;
        }
        User user = new User(username, password, firstName, lastName);
        users.put(username, user);
        saveUsers();
        return true;
    }

    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadUsers() {
        File dataFile = new File(USER_DATA_FILE);
        if (dataFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
                Object obj = ois.readObject();
                if (obj instanceof HashMap<?, ?>) {
                    users = (HashMap<String, User>) obj;
                } else {
                    // Handle the case where the deserialized object isn't what you expected
                    System.err.println("Failed to load user data. Data format not recognized.");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    
    // Get user by username
    public User getUser(String username) {
        return users.get(username);
    }

    // Edit user profile
    public boolean editUserProfile(String username, String newPassword, String newFirstName, String newLastName) {
        if (sessionManager.isLoggedIn() && sessionManager.getCurrentUser().getUsername().equals(username)) {
            User user = getUser(username);
            if (user == null) {
                return false;
            }
            user.setPassword(newPassword);
            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            saveUsers(); // Save users after editing
            return true;
        }
        return false;
    }

    // User login
    public boolean loginUser(String username, String password) {
        User user = getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUsers.put(username, true);
            sessionManager.loginUser(user); // Updating the SessionManager with the logged-in user
            return true; // Login successful
        }
        return false; // Login failed
    }

    // User logout
    public boolean logoutUser(String username) {
        if (loggedInUsers.containsKey(username)) {
            loggedInUsers.remove(username);
            sessionManager.logoutUser(); // Logout the user from the session
            return true; // Logout successful
        }
        return false; // User was not logged in
    }

    // Check if user is logged in
    public boolean isUserLoggedIn(String username) {
        return loggedInUsers.containsKey(username);
    }

    // Remove a user
    public boolean removeUser(String username) {
        if (users.containsKey(username)) {
            users.remove(username);
            saveUsers(); // Save users after removal
            return true;
        }
        return false;
    }
}
