package Frontend;

import Backend.SystemManager;
import Backend.UserDAO;
import Backend.UserInfo;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserInfoController {

    @FXML private TextField nameField, cnicField, addressField, emailField, phoneField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderBox;

    private UserInfo user;

    @FXML
    public void initialize() {
        genderBox.getItems().addAll("Male", "Female", "Other");

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

        user.setName(nameField.getText());
        cnicField.setText(user.getCnic());
        user.setDob(dobPicker.getValue());
        user.setGender(genderBox.getValue());
        user.setAddress(addressField.getText());
        user.setEmail(emailField.getText());
        user.setPhone(phoneField.getText());

        boolean success = UserDAO.updateUser(user);

        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Update Status");
        alert.setHeaderText(null);
        alert.setContentText(success ? "Profile updated successfully!" : "Failed to update profile.");
        alert.showAndWait();
    }
}