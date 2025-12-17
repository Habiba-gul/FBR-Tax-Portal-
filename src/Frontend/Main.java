package Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Correct path: Login.fxml is now in resources/Frontend/
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load());
        
        stage.setTitle("FBR Tax Application - Login");
        stage.setScene(scene);
        stage.setMaximized(true); // Keeps your full-screen feel
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}