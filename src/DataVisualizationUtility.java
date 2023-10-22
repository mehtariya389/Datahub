import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.util.HashMap;

public class DataVisualizationUtility extends Application {

    private static HashMap<String, Post> postsForVisualization;

    public static void launchPieChart(HashMap<String, Post> posts) {
        postsForVisualization = posts;
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add shares for each post to the dataset
        for (Post post : postsForVisualization.values()) {
            pieChartData.add(new PieChart.Data(post.getId(), post.getShares()));
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Distribution of #Shares");

        Scene scene = new Scene(pieChart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
