package Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));

        // Create the scene
        Scene scene = new Scene(loader.load());

        // Configure the stage
        primaryStage.setTitle("FBR Tax Portal - Login");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);  // Full screen as you had before
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}