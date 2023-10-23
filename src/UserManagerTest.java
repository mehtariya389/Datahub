import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class UserManagerTest {

    private UserManager userManager;

    @Before
    public void setUp() throws IOException {
        // Create a fresh instance of UserManager for testing
        resetUserManagerInstance();

        // Clear users HashMap for each test
        clearUsersHashMap();
    }

    @After
    public void tearDown() {
        // No operations needed here since we're working in-memory
    }

    @Test
    public void testCreateUser() {
        Assert.assertTrue(userManager.createUser("johnDoe", "password", "John", "Doe"));
        Assert.assertFalse(userManager.createUser("johnDoe", "password123", "John", "Doe")); // Duplicate username
    }

    @Test
    public void testLoginUser() {
        userManager.createUser("johnDoe", "password", "John", "Doe");
        Assert.assertTrue(userManager.loginUser("johnDoe", "password"));
        Assert.assertFalse(userManager.loginUser("johnDoe", "wrongPassword"));
    }

    @Test
    public void testLogoutUser() {
        userManager.createUser("johnDoe", "password", "John", "Doe");
        userManager.loginUser("johnDoe", "password");
        Assert.assertTrue(userManager.logoutUser("johnDoe"));
        Assert.assertFalse(userManager.logoutUser("johnDoe")); // Already logged out
    }

    @Test
    public void testEditUserProfile() {
        userManager.createUser("johnDoe", "password", "John", "Doe");
        userManager.loginUser("johnDoe", "password");
        Assert.assertTrue(userManager.editUserProfile("johnDoe", "newPassword", "Johnathan", "D"));
    }

    @Test
    public void testRemoveUser() {
        userManager.createUser("johnDoe", "password", "John", "Doe");
        Assert.assertTrue(userManager.removeUser("johnDoe"));
        Assert.assertFalse(userManager.removeUser("johnDoe")); // Already removed
    }

    private void resetUserManagerInstance() {
        try {
            java.lang.reflect.Field instanceField = UserManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
            userManager = UserManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearUsersHashMap() {
        try {
            java.lang.reflect.Field usersField = UserManager.class.getDeclaredField("users");
            usersField.setAccessible(true);
            usersField.set(userManager, new HashMap<String, User>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
