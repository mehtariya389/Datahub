import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;

public class PostManagerScene extends VBox {

    private TextField idField, contentField, authorField, searchField;
    private Button addButton, retrieveButton, removeButton, exportButton, searchButton;
    private ListView<Post> postListView;
    private PostManager postManager;
    private SessionManager sessionManager;

    public PostManagerScene() {
        postManager = PostManager.getInstance();
        sessionManager = SessionManager.getInstance();

        setPadding(new Insets(15));
        setSpacing(15);

        Label titleLabel = new Label("Post Manager");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label idLabel = new Label("ID:");
        idField = new TextField();

        Label contentLabel = new Label("Content:");
        contentField = new TextField();

        Label authorLabel = new Label("Author:");
        authorField = new TextField();

        // Update the authorField to the current user's username if logged in
        if (sessionManager.isLoggedIn()) {
            authorField.setText(sessionManager.getCurrentUser().getUsername());
            authorField.setDisable(true); // Disable editing
        } else {
            showAlert("Notification", "No user is currently logged in. You must log in to post.");
        }

        addButton = new Button("Add Post");
        retrieveButton = new Button("Retrieve Post by ID");
        removeButton = new Button("Remove Post by ID");
        exportButton = new Button("Export Post by ID to CSV");

        gridPane.add(idLabel, 0, 0);
        gridPane.add(idField, 1, 0);
        gridPane.add(contentLabel, 0, 1);
        gridPane.add(contentField, 1, 1);
        gridPane.add(authorLabel, 0, 2);
        gridPane.add(authorField, 1, 2);
        gridPane.add(addButton, 0, 3);
        gridPane.add(retrieveButton, 1, 3);
        gridPane.add(removeButton, 0, 4);
        gridPane.add(exportButton, 1, 4);

        postListView = new ListView<>();
        postListView.setPrefHeight(200);

        searchField = new TextField();
        searchField.setPromptText("Search by ID...");
        searchButton = new Button("Search");

        getChildren().addAll(titleLabel, gridPane, postListView, searchField, searchButton);

        // Event Handlers
        addButton.setOnAction(e -> handleAddPost());
        retrieveButton.setOnAction(e -> handleRetrievePost());
        removeButton.setOnAction(e -> handleRemovePost());
        exportButton.setOnAction(e -> handleExportPost());
        searchButton.setOnAction(e -> handleSearchPost());
    }

    private void handleAddPost() {
        if (!sessionManager.isLoggedIn()) {
            showAlert("Error", "You need to be logged in to add a post!");
            return;
        }

        String id = idField.getText();
        String content = contentField.getText();
        String author = sessionManager.getCurrentUser().getUsername(); // Use the current logged-in user's username

        Post newPost = new Post(id, content, author, 0, 0, LocalDateTime.now());
        if (postManager.addPost(newPost)) {
            postListView.getItems().add(newPost);
        } else {
            showAlert("Error", "Post with the given ID already exists!");
        }
    }

    private void handleRetrievePost() {
        String id = idField.getText();
        Post post = postManager.getPostById(id);
        if (post != null) {
            showAlert("Post Found", post.toString());
        } else {
            showAlert("Error", "No post found with the given ID!");
        }
    }

    private void handleRemovePost() {
        String id = idField.getText();
        if (postManager.removePost(id)) {
            Post toRemove = null;
            for (Post post : postListView.getItems()) {
                if (post.getId().equals(id)) {
                    toRemove = post;
                    break;
                }
            }
            if (toRemove != null) {
                postListView.getItems().remove(toRemove);
            }
            showAlert("Success", "Post removed successfully!");
        } else {
            showAlert("Error", "No post found with the given ID!");
        }
    }

    private void handleExportPost() {
        String id = idField.getText();
        if (postManager.exportPostToCSV(id, "./", "exported_post")) {
            showAlert("Success", "Post exported successfully!");
        } else {
            showAlert("Error", "Failed to export post!");
        }
    }

    private void handleSearchPost() {
        String id = searchField.getText();
        Post post = postManager.getPostById(id);
        if (post != null) {
            postListView.getSelectionModel().select(post);
            postListView.scrollTo(post);
        } else {
            showAlert("Error", "No post found with the given ID!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
