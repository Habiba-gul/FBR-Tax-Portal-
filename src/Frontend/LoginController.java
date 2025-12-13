package Frontend;

import Backend.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private final LoginService loginService = new LoginService(); // backend object

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        boolean success = loginService.validateLogin(username, password);

        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Login Status");
        alert.setHeaderText(null);
        alert.setContentText(success ? "Login Successful ✅" : "Invalid Credentials ❌");
        alert.showAndWait();
    }
}

