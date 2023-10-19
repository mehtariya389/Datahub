
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginWindow loginWindow = new LoginWindow();
        Scene scene = new Scene(loginWindow, 300, 200);
        primaryStage.setTitle("Data Analytics Hub - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
