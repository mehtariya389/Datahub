import java.util.Comparator;
import java.util.List;

public class SortByDateTime implements SortingStrategy {
    @Override
    public List<Post> sort(List<Post> posts) {
        posts.sort(Comparator.comparing(Post::getDateTime).reversed()); // newest first
        return posts;
    }
}