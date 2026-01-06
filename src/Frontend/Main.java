package Frontend;

import javafx.animation.FadeTransition;
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Splash.fxml"));
        Parent splashRoot = loader.load();

        Scene splashScene = new Scene(splashRoot, 800, 600); // fixed window size for splash
        primaryStage.setScene(splashScene);
        primaryStage.setTitle("FBR Tax Portal");
        primaryStage.setResizable(false);
        primaryStage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), splashRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        PauseTransition wait = new PauseTransition(Duration.seconds(5)); // 5 seconds
        wait.setOnFinished(event -> {
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent loginRoot = loginLoader.load();
                Scene loginScene = new Scene(loginRoot);

                primaryStage.setScene(loginScene);
                primaryStage.setTitle("FBR Tax Portal - Login");
                primaryStage.setMaximized(true);
                primaryStage.setResizable(true);
                primaryStage.centerOnScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        wait.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
