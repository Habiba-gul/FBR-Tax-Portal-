package Frontend;

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

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        // --- DEBUG PRINT (Keep this for now!) ---
        System.out.println("DEBUG: Username entered: [" + username + "], Password entered: [" + password + "]"); 
        // -----------------------------------------

        // TEMPORARY BYPASS: Check for "user" and "pass"
        boolean loginSuccessful = (username.trim().equals("user") && password.trim().equals("pass"));

        if (loginSuccessful) {
            System.out.println("Login successful (TEMPORARY BYPASS). Navigating to Dashboard.");
            
            try {
                // 1. Load the User Dashboard FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
                Parent root = loader.load();

                // 2. Get the current Stage from the event source
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                
                // 3. Create new Scene and switch
                Scene scene = new Scene(root, stage.getWidth(), stage.getHeight()); 
                
                stage.setScene(scene);
                stage.setTitle("FBR Tax Application - Dashboard");
                stage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to load UserDashboard.fxml");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Load Error");
                alert.setHeaderText(null);
                alert.setContentText("Could not load the User Dashboard interface.");
                alert.showAndWait();
            }
        } else {
            // Show Alert for failed login
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid credentials. Try 'user' and 'pass'.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
            stage.setTitle("FBR Tax Application - Register");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Register.fxml");
        }
    }
}