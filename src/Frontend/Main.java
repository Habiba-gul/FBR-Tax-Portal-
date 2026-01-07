package Frontend;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("Splash.fxml"));
        Parent splashRoot = splashLoader.load();

        Scene splashScene = new Scene(splashRoot, 800, 600);
        primaryStage.setScene(splashScene);
        primaryStage.setTitle("FBR Tax Portal");
        primaryStage.centerOnScreen();
        primaryStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login.fxml"));
                Parent loginRoot = loginLoader.load();
                Scene loginScene = new Scene(loginRoot, 800, 600);
                primaryStage.setScene(loginScene);
                primaryStage.setTitle("FBR Tax Portal - Login");
                primaryStage.centerOnScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}