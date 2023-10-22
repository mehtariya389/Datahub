import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements Observer,Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
    private String password;
    private String firstName;
    private String lastName;
    private HashMap<String, Post> posts = new HashMap<>();
    
    private List<String> notifications = new ArrayList<>(); // To store notifications for the user

    // Added isVIP attribute
    private boolean isVIP;
    private boolean needsReLogin;

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isVIP = false; // Default value for new users
    }
    
    @Override
    public void update(String message) {
        notifications.add(message);
        displayNotification(message);
    }

    public void displayNotification(String message) {
        System.out.println("Notification for " + getFullName() + ": " + message);
    }
    
    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public HashMap<String, Post> getPosts() {
        return posts;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    // Added getter for isVIP
    public boolean isVIP() {
        return isVIP;
    }
    
    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Added setter for isVIP
    public void setVIP(boolean isVIP) {
        this.isVIP = isVIP;
    }
    
    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addPost(Post post) {
        this.posts.put(post.getId(), post);
    }

    public void removePost(String postId) {
        this.posts.remove(postId);
    }

    public Post getPost(String postId) {
        return this.posts.get(postId);
    }

    public boolean hasPost(String postId) {
        return this.posts.containsKey(postId);
    }

    public boolean needsReLogin() {
        return needsReLogin;
    }

    public void resetReLoginFlag() {
        this.needsReLogin = false;
    }
    
    @Override
    public String toString() {
        return "User [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}
