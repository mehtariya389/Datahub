import java.util.HashMap;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

public class DataVisualizationUtility extends Application {

    private static HashMap<Integer, Post> postsForVisualization; // Note the Integer type for the HashMap key, since the ID is now an int.

    public static void launchPieChart(HashMap<Integer, Post> posts) {
        postsForVisualization = posts;
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add shares for each post to the dataset
        for (Post post : postsForVisualization.values()) {
            // Validate post data before adding to visualization
            if (post.getId() >= 0 && post.getShares() >= 0) { // No null check is required as int cannot be null
                pieChartData.add(new PieChart.Data(String.valueOf(post.getId()), post.getShares())); // Convert id to String for PieChart.Data
            }
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Distribution of #Shares");

        Scene scene = new Scene(pieChart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

