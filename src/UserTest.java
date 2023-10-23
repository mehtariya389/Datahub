import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User("testUser", "testPass", "Test", "User");
    }

    @Test
    public void testGetters() {
        assertEquals("testUser", user.getUsername());
        assertEquals("testPass", user.getPassword());
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertFalse(user.isVIP());
    }

    @Test
    public void testSetters() {
        user.setUsername("newUsername");
        user.setPassword("newPassword");
        user.setFirstName("NewFirst");
        user.setLastName("NewLast");
        user.setVIP(true);

        assertEquals("newUsername", user.getUsername());
        assertEquals("newPassword", user.getPassword());
        assertEquals("NewFirst", user.getFirstName());
        assertEquals("NewLast", user.getLastName());
        assertTrue(user.isVIP());
    }

    @Test
    public void testFullName() {
        assertEquals("Test User", user.getFullName());
    }

    @Test
    public void testAddPost() {
        Post post = new Post(1, "Content", "testUser", 10, 5, "12/12/2022 14:55");
        user.addPost(post);
        assertTrue(user.hasPost(1));
    }

    @Test
    public void testRemovePost() {
        Post post = new Post(2, "Content", "testUser", 10, 5, "12/12/2022 14:55");
        user.addPost(post);
        assertTrue(user.hasPost(2));
        user.removePost(2);
        assertFalse(user.hasPost(2));
    }

    @Test
    public void testGetPost() {
        Post post = new Post(3, "Content", "testUser", 10, 5, "12/12/2022 14:55");
        user.addPost(post);
        assertEquals(post, user.getPost(3));
    }

    @Test
    public void testNotifications() {
        user.update("Test message");
        assertTrue(user.getNotifications().contains("Test message"));
    }

    @Test
    public void testToString() {
        String expected = "User [username=testUser, firstName=Test, lastName=User]";
        assertEquals(expected, user.toString());
    }

    @Test
    public void testReLoginFlag() {
        assertFalse(user.needsReLogin());
        user.setVIP(true);
        assertTrue(user.needsReLogin());
        user.resetReLoginFlag();
        assertFalse(user.needsReLogin());
    }
}

