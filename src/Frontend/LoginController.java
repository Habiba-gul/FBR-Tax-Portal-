package Frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        // This is where you will call Person A's backend validation
        System.out.println("Login clicked! User: " + username);
        
        // Example integration:
        // boolean success = SystemManager.login(username, password);
    }

    @FXML
    private void handleRegister() {
        System.out.println("Register clicked! Switching to Register screen...");
        // Logic to switch scene to Register.fxml goes here
    }
}