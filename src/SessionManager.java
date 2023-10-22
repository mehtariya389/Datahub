
public class SessionManager {

    private static SessionManager instance;  // Singleton instance
    private User currentUser;                // The currently logged-in user

    // Private constructor for singleton pattern
    private SessionManager() {
    }

    // Public method to get the singleton instance
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Store the logged-in user
    public void loginUser(User user) {
        this.currentUser = user;
    }

    // Logout the current user
    public void logoutUser() {
        this.currentUser = null;
    }

    // Check if there is a logged-in user
    public boolean isLoggedIn() {
        return this.currentUser != null;
    }

    // Get the current logged-in user
    public User getCurrentUser() {
        return this.currentUser;
    }
}
