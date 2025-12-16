package Frontend;

import Backend.AppLoginService; // Our new service
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private AppLoginService loginService = new AppLoginService();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        String role = loginService.validateLogin(username, password);

        if (role.equals("ADMIN")) {
            navigateTo(event, "AdminDashboard.fxml", "FBR Admin Portal", 1100, 750);
        } else if (role.equals("USER")) {
            navigateTo(event, "UserDashboard.fxml", "FBR Taxpayer Portal", 800, 600);
        } else {
            showErrorAlert("Login Failed", "Invalid credentials.");
        }
    }

    /**
     * This method was missing! It handles the "Register Now" button.
     */
    @FXML
    private void handleRegister(ActionEvent event) {
        System.out.println("Navigating to Registration...");
        navigateTo(event, "Register.fxml", "FBR Tax Application - Register", 800, 600);
    }

    private void navigateTo(ActionEvent event, String fxmlFile, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, width, height); 
            
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("System Error", "Could not load: " + fxmlFile);
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}