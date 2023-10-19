import java.util.List;

public class SortByLikes implements SortingStrategy {
    @Override
    public List<Post> sort(List<Post> posts) {
        posts.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes())); // descending
        return posts;
    }
}