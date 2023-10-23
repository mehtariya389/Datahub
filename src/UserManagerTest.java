
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UserManagerTest {

    private File tempUserDataFile;
    private UserManager userManager;

    @Before
    public void setUp() throws IOException {
        // Create a temporary file for testing
        tempUserDataFile = File.createTempFile("test_users", ".dat");
        
        // Change the user data file location for testing
        setPrivateUserDataFileField(tempUserDataFile.getAbsolutePath());

        // Create a fresh instance of UserManager for testing
        resetUserManagerInstance();
    }

    @After
    public void tearDown() {
        // Delete the temporary file after tests
        if (tempUserDataFile != null && tempUserDataFile.exists()) {
            tempUserDataFile.delete();
        }
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

    private void setPrivateUserDataFileField(String value) {
        try {
            java.lang.reflect.Field userDataFileField = UserManager.class.getDeclaredField("USER_DATA_FILE");
            userDataFileField.setAccessible(true);
            userDataFileField.set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

