import java.util.List;
import java.util.stream.Collectors;

public class FilterByAuthor implements FilteringStrategy {
    private String author;

    public FilterByAuthor(String author) {
        this.author = author;
    }

    @Override
    public List<Post> filter(List<Post> posts) {
        return posts.stream()
            .filter(post -> post.getAuthor().equalsIgnoreCase(author))
            .collect(Collectors.toList());
    }
}