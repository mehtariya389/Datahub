# Data Analytics Hub Application
## Environment & Tools:
### IDE Used: Eclipse 
### Java Version: Java 17 
### JavaFX Version: JavaFX 21 
## Installation & Running:
1. Clone the Repository.
2. Open the project in your preferred IDE (Eclipse recommended).
3. Set up JavaFX: In your IDE, add the JavaFX SDK as an external library.
4. Make sure to include the JavaFX modules (javafx.controls, javafx.fxml, etc.) to your run configurations VM options.
5. Use your IDE's run button or run the main class directly to start the application.

## OO Design:
### Class Diagram:
A simple textual representation of major classes is shown below

User: Represents individual users. Attributes include username, password, isVIP, etc.

Methods: isVIP(), resetReLoginFlag(), etc.
UserManager: Singleton pattern. Manages user-related operations.

Methods: getUser(), addUser(), etc.
SessionManager: Singleton pattern. Manages the current session's logged-in user.

Methods: loginUser(), getLoggedInUser(), etc.
LoginWindow: JavaFX VBox. Represents the login UI window.

Components: usernameField, passwordField, loginButton, etc.
Methods: handleLogin(), openRegistrationWindow(), etc.
UserDashboard: Represents the main dashboard displayed to logged-in users.
