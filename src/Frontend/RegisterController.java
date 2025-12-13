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

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField cnicField;
    @FXML private TextField serviceProviderField;
    @FXML private TextField cellNumberField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void handleRegistration(ActionEvent event) {
        String cnic = cnicField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 1. Basic Validation (Password Check)
        if (password.isEmpty() || !password.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match or are empty.");
            alert.showAndWait();
            return;
        }

        // --- TEMPORARY SUCCESS SIMULATION ---
        System.out.println("Attempting registration for CNIC: " + cnic);
        
        // Assume success for interface testing:
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Registration successful! You may now log in.");
        successAlert.showAndWait();
        
        // 2. Go back to login screen after successful registration
        handleBackToLogin(event);
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            // Load the Login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            // Get the current Stage and switch Scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
            
            stage.setScene(scene);
            stage.setTitle("FBR Tax Application - Login");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Login.fxml");
        }
    }
}