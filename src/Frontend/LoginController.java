package Frontend;

import Backend.AppLoginService;
import Backend.SystemManager;
import Backend.UserDAO;
import Backend.UserInfo;
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

    @FXML private TextField usernameField;      // This is CNIC / Registration No
    @FXML private PasswordField passwordField;

    private final AppLoginService loginService = new AppLoginService();

    @FXML
    private void handleLogin(ActionEvent event) {
    String cnic = usernameField.getText().trim();
    String password = passwordField.getText().trim();

    if (cnic.isEmpty() || password.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Empty Fields", "Please enter CNIC and Password.");
        return;
    }

    String role = loginService.validateLogin(cnic, password);

    if (role != null) {
        // Fetch full user info and set in SystemManager
        UserInfo user = UserDAO.getUserByCNIC(cnic);
        if (user != null) {
            user.setRole(role);  // Ensure role is set
            SystemManager.setCurrentUser(user);  // <-- THIS IS THE KEY FIX
        }

        String dashboardFxml = "USER".equals(role) ? "UserDashboard.fxml" : "AdminDashboard.fxml";
        String title = "USER".equals(role) ? "FBR Tax Portal - User Dashboard" : "FBR Tax Portal - Admin Dashboard";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardFxml));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dashboard.");
        }
    } else {
        showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid CNIC or Password.");
    }
}

    @FXML
    private void handleRegister(ActionEvent event) {
        navigateTo(event, "Register.fxml", "FBR Tax Portal - Register");
    }

    // Helper method to switch screens
    private void navigateTo(ActionEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot load " + fxmlFile + ". Please check the file.");
        }
    }

    // Helper method for alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}