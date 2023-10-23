
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class PostTest {

    private Post post;

    @Before
    public void setUp() {
        post = new Post(1, "Test Content", "Test Author", 10, 5, "01/01/2021 12:00");
    }

    @Test
    public void testGetId() {
        assertEquals(1, post.getId());
    }

    @Test
    public void testSetId() {
        post.setId(2);
        assertEquals(2, post.getId());
    }

    @Test
    public void testGetContent() {
        assertEquals("Test Content", post.getContent());
    }

    @Test
    public void testSetContent() {
        post.setContent("New Content");
        assertEquals("New Content", post.getContent());
    }

    @Test
    public void testGetAuthor() {
        assertEquals("Test Author", post.getAuthor());
    }

    @Test
    public void testSetAuthor() {
        post.setAuthor("New Author");
        assertEquals("New Author", post.getAuthor());
    }

    @Test
    public void testGetLikes() {
        assertEquals(10, post.getLikes());
    }

    @Test
    public void testIncrementLikes() {
        post.incrementLikes();
        assertEquals(11, post.getLikes());
    }

    @Test
    public void testGetShares() {
        assertEquals(5, post.getShares());
    }

    @Test
    public void testIncrementShares() {
        post.incrementShares();
        assertEquals(6, post.getShares());
    }

    @Test
    public void testGetDateTime() {
        LocalDateTime expectedDate = LocalDateTime.of(2021, 1, 1, 12, 0);
        assertEquals(expectedDate, post.getDateTime());
    }

    @Test
    public void testSetDateTime() {
        LocalDateTime newDate = LocalDateTime.of(2022, 1, 1, 12, 0);
        post.setDateTime(newDate);
        assertEquals(newDate, post.getDateTime());
    }

    @Test
    public void testObserverPattern() {
        MockObserver mockObserver = new MockObserver();

        post.addObserver(mockObserver);
        post.notifyObservers("Update!");

        assertTrue(mockObserver.wasUpdated);
    }

    private static class MockObserver implements Observer {
        boolean wasUpdated = false;

        @Override
        public void update(String message) {
            wasUpdated = true;
        }
    }


    @Test
    public void testToString() {
        String expectedString = "Post [id=1, content=Test Content, author=Test Author, likes=10, shares=5, dateTime=01/01/2021 12:00]";
        assertEquals(expectedString, post.toString());
    }
}
