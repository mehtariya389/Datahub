import java.util.HashMap;

public class UserManager {
    private static UserManager instance;
    private HashMap<String, User> users = new HashMap<>(); // Using username as the key

    // For this simple version, we'll use a hashmap to mock a logged-in session
    private HashMap<String, Boolean> loggedInUsers = new HashMap<>(); 

    private UserManager() {}

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // Create a user profile
    public boolean createUser(String username, String password, String firstName, String lastName) {
        if (users.containsKey(username)) {
            return false; // Username already exists
        }
        User user = new User(username, password, firstName, lastName);
        users.put(username, user);
        return true;
    }

    // Get user by username
    public User getUser(String username) {
        return users.get(username);
    }

    // Edit user profile
    public boolean editUserProfile(String username, String newPassword, String newFirstName, String newLastName) {
        User user = getUser(username);
        if (user == null) {
            return false; // User doesn't exist
        }
        user.setPassword(newPassword);
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        return true;
    }

    // User login
    public boolean loginUser(String username, String password) {
        User user = getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUsers.put(username, true);
            return true; // Login successful
        }
        return false; // Login failed
    }

    // User logout
    public boolean logoutUser(String username) {
        if (loggedInUsers.containsKey(username)) {
            loggedInUsers.remove(username);
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
            return true; // User removed
        }
        return false; // User doesn't exist
    }
}

