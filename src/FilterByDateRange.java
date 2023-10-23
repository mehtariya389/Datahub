import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByDateRange implements FilteringStrategy {
    private LocalDateTime startDate, endDate;

    public FilterByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public List<Post> filter(List<Post> posts) {
        return posts.stream()
            .filter(post -> post.getDateTime().isAfter(startDate) && post.getDateTime().isBefore(endDate))
            .collect(Collectors.toList());
    }
}
