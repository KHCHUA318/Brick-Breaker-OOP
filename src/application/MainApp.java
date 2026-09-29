package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import application.UI.MainController;

public class MainApp extends Application {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/MainView.fxml"));
        Parent root = loader.load();
        
        // Get controller and set primary stage
        MainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        
        // Set up 
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        primaryStage.setTitle("Candy Breaker");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        
        // Show the stage before setting up key controls
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
