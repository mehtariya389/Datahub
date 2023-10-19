
import java.time.LocalDateTime;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PostManagerScene extends VBox {

    private TextField idField, contentField, authorField, searchField;
    private Button addButton, retrieveButton, removeButton, exportButton, searchButton;
    private ListView<Post> postListView;
    private PostManager postManager;

    public PostManagerScene() {
        postManager = PostManager.getInstance();

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
        String id = idField.getText();
        String content = contentField.getText();
        String author = authorField.getText();
        
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
        // For the sake of this example, we'll hardcode a path and filename
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
