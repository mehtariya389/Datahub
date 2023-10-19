import java.time.LocalDateTime;

public class Post {
    private String id;
    private String content;
    private String author;
    private int likes;
    private int shares;
    private LocalDateTime dateTime;

    public Post(String id, String content, String author, int likes, int shares, LocalDateTime dateTime) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        this.dateTime = dateTime;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public int getLikes() {
        return likes;
    }

    public int getShares() {
        return shares;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    // Utility methods
    public void incrementLikes() {
        this.likes++;
    }

    public void incrementShares() {
        this.shares++;
    }

    @Override
    public String toString() {
        return "Post [id=" + id + ", content=" + content + ", author=" + author + ", likes=" + likes + ", shares=" + shares + ", dateTime=" + dateTime + "]";
    }
}
