
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class UserDashboard extends VBox {
    
    private User currentUser;
    private ListView<Post> postListView;
    private ObservableList<Post> observablePosts;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Label infoLabel;
    private Button editProfileButton;
    private Button retrievePostByIdButton;
    private TextField postIdInputField;
    private ComboBox<String> userSelectionComboBox;
    private TextField topNInputField;
    private Button retrieveTopNPostsButton;
    private Button exportPostButton;
    private Button logoutButton;
    

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
        editProfileButton = new Button("Edit Profile");
        getChildren().add(editProfileButton);
        retrievePostByIdButton = new Button("Retrieve Post");
        userSelectionComboBox = new ComboBox<>();
        userSelectionComboBox.getItems().addAll("Current User", "ALL Users");
        userSelectionComboBox.setValue("Current User");
        exportPostButton = new Button("Export Post");
        getChildren().add(exportPostButton);
        logoutButton = new Button("Logout");
        getChildren().add(logoutButton);
        
        topNInputField = new TextField();
        topNInputField.setPromptText("Enter value for N");
        
        retrieveTopNPostsButton = new Button("Retrieve Top N Posts");
        HBox topNLayout = new HBox(10, userSelectionComboBox, topNInputField, retrieveTopNPostsButton);
        
        getChildren().add(topNLayout);
        
        
        infoLabel = new Label();
        
        // Add components to the VBox layout
        getChildren().addAll(titleLabel, postListView, buttonBox, infoLabel);
        
        // TextField for user to input the post ID
        postIdInputField = new TextField();
        postIdInputField.setPromptText("Enter Post ID");
        
        // Layout to position the input field and the button
        HBox retrievePostLayout = new HBox(10);
        retrievePostLayout.getChildren().addAll(postIdInputField, retrievePostByIdButton);
        getChildren().add(retrievePostLayout);
        

        // Event Handlers
        addButton.setOnAction(e -> handleAddPost());
        editButton.setOnAction(e -> handleEditPost());
        deleteButton.setOnAction(e -> handleDeletePost());
        editProfileButton.setOnAction(e -> handleEditProfile());
        retrievePostByIdButton.setOnAction(e -> handleRetrievePostById());
        retrieveTopNPostsButton.setOnAction(e -> handleRetrieveTopNPosts());
        exportPostButton.setOnAction(e -> handleExportPost());
        logoutButton.setOnAction(e -> handleLogout());
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
    
    private void handleEditProfile() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Update Your Profile Details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField firstName = new TextField(currentUser.getFirstName());
        TextField lastName = new TextField(currentUser.getLastName());
        TextField username = new TextField(currentUser.getUsername());
        PasswordField password = new PasswordField();
        password.setPromptText("New Password (Leave blank to keep old)");

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstName, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastName, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(username, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(password, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                User updatedUser = new User(username.getText(), password.getText().isEmpty() ? currentUser.getPassword() : password.getText(), firstName.getText(), lastName.getText());
                return updatedUser;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedUser -> {
            currentUser.setFirstName(updatedUser.getFirstName());
            currentUser.setLastName(updatedUser.getLastName());
            currentUser.setUsername(updatedUser.getUsername());
            if (!updatedUser.getPassword().isEmpty()) {
                currentUser.setPassword(updatedUser.getPassword());
            }
            // You may also need to handle any username changes in PostManager or other parts of your system.
            infoLabel.setText("Profile updated successfully!");
        });
    }
    
    private void handleRetrievePostById() {
        String postId = postIdInputField.getText().trim();
        if (!postId.isEmpty()) {
            Post post = PostManager.getInstance().getPostById(postId);
            if (post != null) {
                infoLabel.setText("Post details:\n"
                                  + "Content: " + post.getContent() + "\n"
                                  + "Author: " + post.getAuthor() + "\n"
                                  + "Likes: " + post.getLikes() + "\n"
                                  + "Shares: " + post.getShares());
            } else {
                infoLabel.setText("No post found with the provided ID.");
            }
        } else {
            infoLabel.setText("Please enter a post ID.");
        }
    }
    
    private void handleRetrieveTopNPosts() {
        try {
            int n = Integer.parseInt(topNInputField.getText());
            List<Post> topNPosts;
            if (userSelectionComboBox.getValue().equals("Current User")) {
                topNPosts = PostManager.getInstance().getTopNPostsByLikesForUser(n, currentUser.getUsername());
            } else {
                topNPosts = PostManager.getInstance().getTopNPostsByLikes(n);
            }
            observablePosts.clear();
            observablePosts.addAll(topNPosts);
            infoLabel.setText("Retrieved top " + n + " posts.");
        } catch (NumberFormatException e) {
            infoLabel.setText("Please enter a valid number for N.");
        }
    }
    
    private void handleExportPost() {
        Post selectedPost = postListView.getSelectionModel().getSelectedItem();
        if (selectedPost != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Post to CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            
            // Show save file dialog
            File file = fileChooser.showSaveDialog(null);
            
            if (file != null) {
                boolean success = PostManager.getInstance().exportPostToCSV(selectedPost.getId(), file.getParent(), file.getName());
                if (success) {
                    infoLabel.setText("Post exported successfully!");
                } else {
                    infoLabel.setText("There was an issue exporting the post.");
                }
            }
        } else {
            infoLabel.setText("Please select a post to export!");
        }
    }
    
    private void handleLogout() {
        // Call SessionManager to logout the user
        SessionManager.getInstance().logoutUser();

        // Close the current dashboard window
        Stage currentStage = (Stage) this.getScene().getWindow();
        currentStage.close();

        // Open the login window
        Stage loginStage = new Stage();
        LoginWindow loginWindow = new LoginWindow();
        Scene loginScene = new Scene(loginWindow);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Login");
        loginStage.show();
    }

}

