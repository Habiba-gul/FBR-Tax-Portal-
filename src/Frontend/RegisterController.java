package Frontend;

import Backend.DBconnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.sql.*;

public class RegisterController {

    @FXML private TextField cnicField;
    @FXML private TextField nameField;
    @FXML private TextField serviceProviderField;
    @FXML private TextField cellNumberField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    // ================= REGISTER BUTTON =================
    @FXML
    private void handleRegistration() {

        String cnic = cnicField.getText();
        String name = nameField.getText();
        String phone = cellNumberField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (cnic.isEmpty() || name.isEmpty() || phone.isEmpty()
                || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required");
            return;
        }

        if (!password.equals(confirm)) {
            showAlert("Error", "Passwords do not match");
            return;
        }

        try (Connection conn = DBconnection.getConnection()) {

            // 1️⃣ Insert user
            String userSQL =
                    "INSERT INTO users (name, cnic, email, phone, password, role) " +
                    "VALUES (?, ?, ?, ?, ?, 'USER')";

            PreparedStatement ps = conn.prepareStatement(
                    userSQL, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            ps.setString(2, cnic);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, password);
            ps.executeUpdate();

            // 2️⃣ Get generated user ID
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int userId = rs.getInt(1);

            // 3️⃣ Create taxpayer profile
            String profileSQL =
                    "INSERT INTO taxpayer_profile (user_id) VALUES (?)";

            PreparedStatement ps2 = conn.prepareStatement(profileSQL);
            ps2.setInt(1, userId);
            ps2.executeUpdate();

            showAlert("Success", "Registration completed successfully!");

            clearFields();

            // 4️⃣ Navigate to Login screen automatically
            navigateToLogin();

        } catch (SQLIntegrityConstraintViolationException e) {
            showAlert("Error", "CNIC already exists!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error!");
        }
    }

    // ================= BACK TO LOGIN =================
    @FXML
    private void handleBackToLogin() {
        navigateToLogin();
    }

    // ================= NAVIGATION =================
    private void navigateToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) cnicField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HELPERS =================
    private void clearFields() {
        cnicField.clear();
        nameField.clear();
        serviceProviderField.clear();
        cellNumberField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
