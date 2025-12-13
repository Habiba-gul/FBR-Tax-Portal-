package Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Loads the Login.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        
        // Sets the initial scene size
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        
        stage.setTitle("FBR Tax Application - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}