package Frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class UserInfoController {

    @FXML private TextField nameField, cnicField, emailField, phoneField;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        // In the future, you will call Person A's backend here:
        // User currentUser = SystemManager.getCurrentUser();
        // nameField.setText(currentUser.getName());
        
        // TEMPORARY MOCK DATA for testing:
        nameField.setText("John Doe");
        cnicField.setText("42101-1234567-1");
        emailField.setText("john.doe@example.com");
        phoneField.setText("0300-1234567");
    }

    @FXML
    private void handleUpdate() {
        // Logic to send data to Person A's SystemManager
        System.out.println("Updating database for CNIC: " + cnicField.getText());
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Profile Updated Successfully in the Database!");
        alert.showAndWait();
    }

    @FXML
    private void handleBack(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UserDashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 750));
        stage.show();
    }
}
