import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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
        registerButton.setOnAction(e -> openRegistrationWindow());
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
            // Successful login, set the logged-in user in SessionManager
            SessionManager.getInstance().loginUser(user);
            
            // Reset the re-login flag for the user after successful login
            user.resetReLoginFlag();

            // Check if the user is not a VIP and prompt for upgrade
            if (!user.isVIP()) {
                Alert upgradeAlert = new Alert(AlertType.CONFIRMATION, "Would you like to upgrade to VIP?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = upgradeAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.YES) {
                    user.setVIP(true);
                    infoLabel.setText("Congratulations! You are now a VIP member.");
                }
            }

            // Transition to dashboard
            UserDashboard dashboard = new UserDashboard(user);
            Scene scene = new Scene(dashboard, 600, 400);
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
        } else {
            infoLabel.setText("Invalid login credentials!");
        }
    }


    private void openRegistrationWindow() {
        RegistrationWindow registrationWindow = new RegistrationWindow();
        Scene scene = new Scene(registrationWindow, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Data Analytics Hub - Registration");
        stage.show();
    }
}


