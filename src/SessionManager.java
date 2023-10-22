
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

    // Store the logged-in user and fetch the isVIP status
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

    // Check if the current logged-in user is VIP
    public boolean isCurrentUserVIP() {
        if (this.currentUser != null) {
            return this.currentUser.isVIP();
        }
        return false;
    }

    // Method to allow/disallow VIP functionalities
    public void accessVIPFunctionality() {
        if (!isCurrentUserVIP()) {
            throw new SecurityException("Access Denied: Only VIP users can access this functionality.");
        }
        // Continue with the VIP functionality...
    }
}
