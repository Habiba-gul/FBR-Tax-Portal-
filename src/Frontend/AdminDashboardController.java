package Frontend;

import Backend.AdminService;
import Backend.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AdminDashboardController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> cnicColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> statusColumn;
    @FXML private TableColumn<User, Double> penaltyColumn;

    @FXML private Label selectedNameLabel;
    @FXML private Label selectedCnicLabel;
    @FXML private Label selectedStatusLabel;

    private final AdminService adminService = new AdminService();

    // ================= INITIALIZE =================
@FXML
public void initialize() {
    cnicColumn.setCellValueFactory(new PropertyValueFactory<>("cnic"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    penaltyColumn.setCellValueFactory(new PropertyValueFactory<>("penalty"));

    loadAllUsers();

    userTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) updateControlPanel(newVal);
                else clearControlPanel();
            }
    );
}
    // ================= LOADERS =================
   private void loadAllUsers() {
    ObservableList<User> users = adminService.getAllUsers();
    System.out.println("Loaded " + users.size() + " users from database");
    userTable.setItems(users);
}

    // ================= UI HELPERS =================
    private void updateControlPanel(User user) {
        selectedNameLabel.setText("Name: " + user.getName());
        selectedCnicLabel.setText("CNIC: " + user.getCnic());
        selectedStatusLabel.setText("Status: " + user.getStatus());
    }

    private void clearControlPanel() {
        selectedNameLabel.setText("Name: None Selected");
        selectedCnicLabel.setText("CNIC: N/A");
        selectedStatusLabel.setText("Status: N/A");
    }

    // ================= BUTTON ACTIONS =================

    @FXML
    private void handleViewAll() {
        loadAllUsers();
    }

    @FXML
    private void handleViewDefaulters() {
        userTable.setItems(adminService.getDefaulterList());
    }

    @FXML
    private void handlePenalty() {

        User selected = userTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a taxpayer first.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Penalty");
        alert.setHeaderText("Apply penalty to " + selected.getName());
        alert.setContentText("Penalty will be calculated automatically by system.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            adminService.applyPenalty(selected.getCnic());
            loadAllUsers();
        }
    }

    @FXML
    private void handleOverride() {

        User selected = userTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a taxpayer.");
            return;
        }

        adminService.overridePenalty(selected.getCnic());
        loadAllUsers();
    }

    @FXML
    private void handleSuspend() {

        User selected = userTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a taxpayer.");
            return;
        }

        adminService.suspendUser(selected.getCnic());
        loadAllUsers();
    }

    // ================= NAVIGATION =================
    @FXML
    private void handleTaxSettings() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("TaxRateSettings.fxml"));
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FBR Admin - Tax Rate Settings");
            stage.centerOnScreen();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Error", "Unable to open Tax Rate Settings.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("Login.fxml"));
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FBR Tax Portal - Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= ALERT =================
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
}
