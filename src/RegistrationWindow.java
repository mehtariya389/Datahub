import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationWindow extends VBox {
    
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField firstNameField;
    private TextField lastNameField;
    private Button registerButton;
    private Button backButton;
    private Label infoLabel;

    public RegistrationWindow() {
        setPadding(new Insets(15));
        setSpacing(15);

        Label titleLabel = new Label("Data Analytics Hub - Registration");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        Label firstNameLabel = new Label("First Name:");
        firstNameField = new TextField();
        firstNameField.setPromptText("Enter First Name");

        Label lastNameLabel = new Label("Last Name:");
        lastNameField = new TextField();
        lastNameField.setPromptText("Enter Last Name");

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(firstNameLabel, 0, 2);
        gridPane.add(firstNameField, 1, 2);
        gridPane.add(lastNameLabel, 0, 3);
        gridPane.add(lastNameField, 1, 3);

        registerButton = new Button("Register");
        backButton = new Button("Back to Login");

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(registerButton, backButton);

        infoLabel = new Label();

        getChildren().addAll(titleLabel, gridPane, buttonBox, infoLabel);

        registerButton.setOnAction(e -> handleRegistration());
        backButton.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            LoginWindow loginWindow = new LoginWindow();
            Scene scene = new Scene(loginWindow, 600, 400);
            stage.setScene(scene);
            stage.setTitle("Data Analytics Hub - Login");
        });
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            infoLabel.setText("All fields are mandatory for registration!");
            return;
        }

        if (UserManager.getInstance().getUser(username) != null) {
            infoLabel.setText("Username already exists!");
            return;
        }

        boolean success = UserManager.getInstance().createUser(username, password, firstName, lastName);

        if(success) {
            infoLabel.setText("User registered successfully! Please go back and login.");
        } else {
            infoLabel.setText("Error during registration. Try again.");
        }
    }
}
