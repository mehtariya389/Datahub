import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow extends VBox {
    
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private Label infoLabel;

    public LoginWindow() {
        setPadding(new Insets(15));
        setSpacing(15);

        Label titleLabel = new Label("Data Analytics Hub - Login");
        
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        
        HBox buttonBox = new HBox(10);
        loginButton = new Button("Login");
        registerButton = new Button("Register");
        buttonBox.getChildren().addAll(loginButton, registerButton);
        
        infoLabel = new Label();
        
        // Add components to the VBox layout
        getChildren().addAll(titleLabel, gridPane, buttonBox, infoLabel);

        // Event Handlers
        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegistration());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            infoLabel.setText("Both fields are mandatory!");
            return;
        }
        
        User user = UserManager.getInstance().getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            // Successful login, transition to dashboard
            UserDashboard dashboard = new UserDashboard(user);
            Scene scene = new Scene(dashboard, 600, 400);
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
        } else {
            infoLabel.setText("Invalid login credentials!");
        }
    }

    private void handleRegistration() {
        // Logic for user registration
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            infoLabel.setText("Both fields are mandatory for registration!");
            return;
        }

        if (UserManager.getInstance().getUser(username) != null) {
            infoLabel.setText("Username already exists!");
            return;
        }

        // Using the createUser method for registration
        boolean success = UserManager.getInstance().createUser(username, password, "", ""); // Dummy values for first name and last name

        if(success) {
            infoLabel.setText("User registered successfully! Please login.");
        } else {
            infoLabel.setText("Error during registration. Try again.");
        }
    }
}

