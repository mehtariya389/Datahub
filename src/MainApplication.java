import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scene scene;
        User loggedInUser = SessionManager.getInstance().getCurrentUser(); // hypothetical SessionManager
        
        if (loggedInUser != null) {
            // User already logged in, show dashboard
            UserDashboard dashboard = new UserDashboard(loggedInUser);
            scene = new Scene(dashboard, 600, 400); // adjust size as needed
            primaryStage.setTitle("Data Analytics Hub - Dashboard");
        } else {
            // No user logged in, show login
            LoginWindow loginWindow = new LoginWindow();
            scene = new Scene(loginWindow, 300, 200);
            primaryStage.setTitle("Data Analytics Hub - Login");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
