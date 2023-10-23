import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Post implements Observable{
    private int id;
    private String content;
    private String author;
    private int likes;
    private int shares;
    private LocalDateTime dateTime;
    
    // List of observers observing this Post
    private List<Observer> observers = new ArrayList<>();
    
    public Post(int id, String content, String author, int likes, int shares, String dateStr) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.dateTime = LocalDateTime.parse(dateStr, formatter); // Parse the provided date string to LocalDateTime
    }

    // Getters
    public int getId() {
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
    public void setId(int id) {
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

    // Observable methods:
    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Post [id=" + id + ", content=" + content + ", author=" + author + ", likes=" + likes + ", shares=" + shares + ", dateTime=" + dateTime.format(formatter) + "]";
    }
}
