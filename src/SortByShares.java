import java.util.List;

public class SortByShares implements SortingStrategy {
    @Override
    public List<Post> sort(List<Post> posts) {
        posts.sort((p1, p2) -> Integer.compare(p2.getShares(), p1.getShares())); // descending
        return posts;
    }
}
