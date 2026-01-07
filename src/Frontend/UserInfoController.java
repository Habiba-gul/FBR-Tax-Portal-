package Frontend;

import Backend.SystemManager;
import Backend.UserDAO;
import Backend.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class UserInfoController {

    @FXML private TextField nameField, cnicField, addressField, emailField, phoneField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderBox;

    private UserInfo user;

    @FXML
    public void initialize() {
        genderBox.getItems().addAll("Male", "Female", "Other");

        // Retrieve the logged-in user's data from our session manager
        user = SystemManager.getCurrentUser();

        if (user != null) {
            nameField.setText(user.getName());
            cnicField.setText(user.getCnic());
            dobPicker.setValue(user.getDob());
            genderBox.setValue(user.getGender());
            addressField.setText(user.getAddress());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone());
        }
    }

    @FXML
    private void handleUpdate() {
        if (user == null) return;

        // Update the user object with the values from the text fields
        user.setName(nameField.getText());
        user.setDob(dobPicker.getValue());
        user.setGender(genderBox.getValue());
        user.setAddress(addressField.getText());
        user.setEmail(emailField.getText());
        user.setPhone(phoneField.getText());

        // Save these changes back to the MySQL database
        boolean success = UserDAO.updateUser(user);

        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Update Status");
        alert.setHeaderText(null);
        alert.setContentText(success ? "Profile updated successfully!" : "Failed to update profile.");
        alert.showAndWait();
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("FBR Tax Portal - User Dashboard");
        stage.centerOnScreen();
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}