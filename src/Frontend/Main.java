package Frontend; // Make sure this matches your package structure

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Loads the Login.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        
        // Sets the scene size (Width: 800, Height: 600)
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        
        stage.setTitle("FBR Tax Application");
        stage.setScene(scene);
        stage.setResizable(false); // Optional: keeps the window size fixed
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
