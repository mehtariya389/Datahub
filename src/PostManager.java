import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PostManager {
    private static PostManager instance;
    private HashMap<String, Post> posts = new HashMap<>(); // Map of ID to Post

    private SortingStrategy sortingStrategy;
    private FilteringStrategy filteringStrategy;
    
    private PostManager() {}

    public static PostManager getInstance() {
        if (instance == null) {
            instance = new PostManager();
        }
        return instance;
    }

    public void setSortingStrategy(SortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
    }

    public void setFilteringStrategy(FilteringStrategy filteringStrategy) {
        this.filteringStrategy = filteringStrategy;
    }
    
    // Add a post to the collection
    public boolean addPost(Post post) {
        if (posts.containsKey(post.getId())) {
            // Post ID already exists
            return false;
        }
        posts.put(post.getId(), post);
        return true;
    }

    // Retrieve a post by its ID
    public Post getPostById(String id) {
        return posts.get(id);
    }

    // Remove a post by its ID
    public boolean removePost(String id) {
        if (posts.containsKey(id)) {
            posts.remove(id);
            return true;
        }
        return false;
    }

    // Get top N posts by likes
    public List<Post> getTopNPostsByLikes(int n) {
        return posts.values().stream()
                .sorted(Comparator.comparingInt(Post::getLikes).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    // Get top N posts by likes for a specific user
    public List<Post> getTopNPostsByLikesForUser(int n, String username) {
        return posts.values().stream()
                .filter(post -> post.getAuthor().equals(username))
                .sorted(Comparator.comparingInt(Post::getLikes).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    // Export a post to CSV based on its ID
    public boolean exportPostToCSV(String id, String path, String fileName) {
        Post post = getPostById(id);
        if (post == null) {
            return false; // Post doesn't exist
        }

        try (FileWriter writer = new FileWriter(path + "/" + fileName + ".csv")) {
            writer.append("ID,Content,Author,Likes,Shares,DateTime\n");
            writer.append(post.getId())
                  .append(",")
                  .append(post.getContent())
                  .append(",")
                  .append(post.getAuthor())
                  .append(",")
                  .append(Integer.toString(post.getLikes()))
                  .append(",")
                  .append(Integer.toString(post.getShares()))
                  .append(",")
                  .append(post.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                  .append("\n");
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Error while writing to file
        }
        return true;
    }
    
    // This method gives the sorted and filtered posts
    public List<Post> getProcessedPosts() {
        List<Post> processedPosts = new ArrayList<>(posts.values());

        if (filteringStrategy != null) {
            processedPosts = filteringStrategy.filter(processedPosts);
        }

        if (sortingStrategy != null) {
            processedPosts = sortingStrategy.sort(processedPosts);
        }

        return processedPosts;
    }
    
    // Import posts from a CSV file
    public boolean importPostsFromCSV(String pathToFile) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null || !currentUser.isVIP()) {
            System.err.println("Error: Only VIP users can access this feature.");
            return false;
        }
        try {
            List<Post> postsFromCSV = CSVImportUtility.readCSV(pathToFile);
            for (Post post : postsFromCSV) {
                addPost(post);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Error during import
        }
    }

    
    public void visualizeSharesDistribution() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null || !currentUser.isVIP()) {
            System.err.println("Error: Only VIP users can access this feature.");
            return;
        }
        DataVisualizationUtility.launchPieChart(posts);
    }

}

