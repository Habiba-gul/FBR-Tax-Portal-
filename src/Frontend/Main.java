package Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        
        // No fixed size here; let the layout handle it! 🌊
        Scene scene = new Scene(fxmlLoader.load());
        
        stage.setTitle("FBR Tax Application - Login");
        stage.setScene(scene);
        
        // This ensures it fits your laptop screen beautifully on launch 🚀
        stage.setMaximized(true); 
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}