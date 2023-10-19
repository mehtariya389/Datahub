import java.util.List;

public interface FilteringStrategy {
    List<Post> filter(List<Post> posts);
}