
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;

public class UserDashboard extends VBox {
    
    private User currentUser;
    private ListView<Post> postListView;
    private ObservableList<Post> observablePosts;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Label infoLabel;

    public UserDashboard(User user) {
        this.currentUser = user;

        setPadding(new Insets(15));
        setSpacing(15);

        Label titleLabel = new Label("Welcome, " + currentUser.getFullName());

        observablePosts = FXCollections.observableArrayList(PostManager.getInstance().getTopNPostsByLikesForUser(10, currentUser.getUsername()));
        postListView = new ListView<>(observablePosts);
        
        HBox buttonBox = new HBox(10);
        addButton = new Button("Add Post");
        editButton = new Button("Edit Post");
        deleteButton = new Button("Delete Post");
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton);
        
        infoLabel = new Label();
        
        // Add components to the VBox layout
        getChildren().addAll(titleLabel, postListView, buttonBox, infoLabel);

        // Event Handlers
        addButton.setOnAction(e -> handleAddPost());
        editButton.setOnAction(e -> handleEditPost());
        deleteButton.setOnAction(e -> handleDeletePost());
    }

    private void handleAddPost() {
        TextInputDialog dialog = new TextInputDialog("Post content");
        dialog.setTitle("New Post");
        dialog.setHeaderText("Add a New Post");
        dialog.setContentText("Enter your post content:");

        // Grab the result and add a new post
        dialog.showAndWait().ifPresent(content -> {
            if (!content.trim().isEmpty()) {
                Post newPost = new Post(String.valueOf(content.hashCode()), content, currentUser.getUsername(), 0, 0, LocalDateTime.now());
                PostManager.getInstance().addPost(newPost);
                currentUser.addPost(newPost); // Add to the user's collection
                observablePosts.add(newPost);
                infoLabel.setText("Post added successfully!");
            } else {
                infoLabel.setText("Post content cannot be empty!");
            }
        });
    }

    private void handleEditPost() {
        Post selectedPost = postListView.getSelectionModel().getSelectedItem();
        if (selectedPost != null && selectedPost.getAuthor().equals(currentUser.getUsername())) {
            TextInputDialog dialog = new TextInputDialog(selectedPost.getContent());
            dialog.setTitle("Edit Post");
            dialog.setHeaderText("Edit Your Post");
            dialog.setContentText("Modify your post content:");

            // Grab the result and update the post
            dialog.showAndWait().ifPresent(content -> {
                if (!content.trim().isEmpty()) {
                    selectedPost.setContent(content);
                    infoLabel.setText("Post edited successfully!");
                } else {
                    infoLabel.setText("Post content cannot be empty!");
                }
            });
        } else {
            infoLabel.setText("Please select a post to edit!");
        }
    }

    private void handleDeletePost() {
        Post selectedPost = postListView.getSelectionModel().getSelectedItem();
        if (selectedPost != null && selectedPost.getAuthor().equals(currentUser.getUsername())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Post");
            alert.setHeaderText("Delete Your Post");
            alert.setContentText("Are you sure you want to delete this post?");

            // Grab the result and delete the post
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    PostManager.getInstance().removePost(selectedPost.getId());
                    currentUser.removePost(selectedPost.getId()); // Remove from the user's collection
                    observablePosts.remove(selectedPost);
                    infoLabel.setText("Post deleted successfully!");
                }
            });
        } else {
            infoLabel.setText("Please select a post to delete!");
        }
    }
}

