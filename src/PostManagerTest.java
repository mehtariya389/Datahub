
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class PostManagerTest {

    private PostManager postManager;

    @Before
    public void setUp() {
        postManager = PostManager.getInstance();
    }

    @Test
    public void testAddPost() {
        Post post = new Post(1, "Content", "Author", 10, 5, "12/12/2022 14:55");
        assertTrue(postManager.addPost(post));
        assertFalse(postManager.addPost(post)); // Same post ID should fail
    }

    @Test
    public void testGetPostById() {
        Post post = new Post(2, "Content", "Author", 10, 5, "12/12/2022 14:55");
        postManager.addPost(post);
        assertEquals(post, postManager.getPostById(2));
        assertNull(postManager.getPostById(999)); // ID not in map
    }

    @Test
    public void testRemovePost() {
        Post post = new Post(3, "Content", "Author", 10, 5, "12/12/2022 14:55");
        postManager.addPost(post);
        assertTrue(postManager.removePost(3));
        assertFalse(postManager.removePost(3)); // Post already removed
    }

    @Test
    public void testGetTopNPostsByLikes() {
        Post post1 = new Post(4, "Content", "Author1", 15, 5, "12/12/2022 14:55");
        Post post2 = new Post(5, "Content", "Author2", 25, 5, "12/12/2022 15:55");
        postManager.addPost(post1);
        postManager.addPost(post2);
        List<Post> topPosts = postManager.getTopNPostsByLikes(1);
        assertEquals(1, topPosts.size());
        assertEquals(post2, topPosts.get(0));
    }

    @Test
    public void testGetTopNPostsByLikesForUser() {
        Post post1 = new Post(6, "Content", "Author3", 5, 5, "12/12/2022 14:55");
        Post post2 = new Post(7, "Content", "Author3", 20, 5, "12/12/2022 15:55");
        postManager.addPost(post1);
        postManager.addPost(post2);
        List<Post> topPostsForUser = postManager.getTopNPostsByLikesForUser(1, "Author3");
        assertEquals(1, topPostsForUser.size());
        assertEquals(post2, topPostsForUser.get(0));
    }

}
