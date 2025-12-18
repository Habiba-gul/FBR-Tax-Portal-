package Frontend;

import Backend.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    // Ye sab fields ke naam exactly fxml ke fx:id se match karne chahiye
    @FXML private TextField cnicField;
    @FXML private TextField nameField;
    @FXML private TextField serviceProviderField;
    @FXML private TextField cellNumberField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void handleRegistration(ActionEvent event) {
        // Get values
        String cnic = cnicField.getText().trim();
        String name = nameField.getText().trim();
        String service = serviceProviderField.getText().trim();
        String phone = cellNumberField.getText().trim();
        String email = emailField.getText().trim();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        // Basic validation
        if (cnic.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }
        if (!pass.equals(confirm)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }

        
        // Save to database
        boolean success = UserDAO.registerUser(name, cnic, email, phone, pass);

        if (success) {
            showAlert("Success!", "Registration completed!\nYou can now login with your CNIC and password.");
            goToLogin(event);  // Back to login screen
        } else {
            showAlert("Failed", "Registration failed!\nCNIC may already exist or database error.");
        }
        
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        goToLogin(event);
    }

    private void goToLogin(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.setTitle("FBR Tax Portal - Login");
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Cannot go back to login.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}