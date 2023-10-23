import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostManagerScene extends VBox {

    private TextField idField, contentField, authorField, searchField;
    private Button addButton, retrieveButton, removeButton, exportButton, searchButton;
    private ListView<Post> postListView;
    private PostManager postManager;
    private SessionManager sessionManager;
    private Button importButton;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


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
        importButton = new Button("Import Posts from CSV");


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

        getChildren().addAll(titleLabel, gridPane, postListView, searchField, searchButton, exportButton, importButton);

        // Event Handlers
        addButton.setOnAction(e -> handleAddPost());
        retrieveButton.setOnAction(e -> handleRetrievePost());
        removeButton.setOnAction(e -> handleRemovePost());
        exportButton.setOnAction(e -> handleExportPost());
        searchButton.setOnAction(e -> handleSearchPost());
        importButton.setOnAction(e -> handleImportPosts());
    }

    private void handleAddPost() {
        if (!sessionManager.isLoggedIn()) {
            showAlert("Error", "You need to be logged in to add a post!");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID. Please enter a valid integer ID.");
            return;
        }

        String content = contentField.getText();
        String author = sessionManager.getCurrentUser().getUsername(); // Use the current logged-in user's username

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dateStr = LocalDateTime.now().format(formatter);
        Post newPost = new Post(id, content, author, 0, 0, dateStr);

        if (postManager.addPost(newPost)) {
            postListView.getItems().add(newPost);
        } else {
            showAlert("Error", "Post with the given ID already exists!");
        }
    }

    private void handleRetrievePost() {
        int id;
        try {
            id = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID. Please enter a valid integer ID.");
            return;
        }

        Post post = postManager.getPostById(id);
        if (post != null) {
            showAlert("Post Found", post.toString());
        } else {
            showAlert("Error", "No post found with the given ID!");
        }
    }

    private void handleRemovePost() {
        int id;
        try {
            id = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID. Please enter a valid integer ID.");
            return;
        }

        if (postManager.removePost(id)) {
            Post toRemove = null;
            for (Post post : postListView.getItems()) {
                if (post.getId() == id) {
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
        int id;
        try {
            id = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID. Please enter a valid integer ID.");
            return;
        }

        if (postManager.exportPostToCSV(id, "./", "exported_post")) {
            showAlert("Success", "Post exported successfully!");
        } else {
            showAlert("Error", "Failed to export post!");
        }
    }

    private void handleSearchPost() {
        int id;
        try {
            id = Integer.parseInt(searchField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID. Please enter a valid integer ID.");
            return;
        }

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
    
    private void handleImportPosts() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            boolean success = postManager.importPostsFromCSV(selectedFile.getAbsolutePath());
            if (success) {
                showAlert("Success", "Posts imported successfully from " + selectedFile.getName());
                postListView.getItems().clear(); // Clear the current list
                postListView.getItems().addAll(postManager.getProcessedPosts()); // Reload the updated list
            } else {
                showAlert("Error", "Error importing posts from " + selectedFile.getName());
            }
        }
    }
    


}

