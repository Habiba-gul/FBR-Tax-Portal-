package Frontend;

import Backend.AdminService;
import Backend.User;
import javafx.event.ActionEvent;
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

    private AdminService adminService = new AdminService();

    @FXML
    public void initialize() {
        cnicColumn.setCellValueFactory(new PropertyValueFactory<>("cnic"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        penaltyColumn.setCellValueFactory(new PropertyValueFactory<>("penalty"));

        userTable.setItems(adminService.getAllUsers());

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateControlPanel(newVal);
            } else {
                clearControlPanel();
            }
        });
    }

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

    @FXML
    private void handleViewAll() {
        userTable.setItems(adminService.getAllUsers());
    }

    @FXML
    private void handleViewDefaulters() {
        userTable.setItems(adminService.getDefaulterList());
    }

    @FXML
    private void handleOverride() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Paid");
            selected.setPenalty(0.0);
            userTable.refresh();
            updateControlPanel(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a taxpayer to override.");
        }
    }

    @FXML
    private void handlePenalty() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a taxpayer to issue penalty.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Penalty");
        alert.setHeaderText("Issue 10% Penalty to " + selected.getName() + "?");
        alert.setContentText("This will add " + (selected.getBaseTax() * 0.10) + " to their penalty.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            adminService.applyTenPercentPenalty(selected);
            userTable.refresh();
            updateControlPanel(selected);
        }
    }

    @FXML
    private void handleSuspend() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Suspended");
            userTable.refresh();
            updateControlPanel(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a taxpayer to suspend.");
        }
    }

    @FXML
    private void handleTaxSettings() {
        try {
            // Relative path — works when TaxRateSettings.fxml is in resources/Frontend/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaxRateSettings.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) userTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FBR Admin - Tax Rate Settings");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open Tax Rate Settings.\nEnsure TaxRateSettings.fxml is in resources/Frontend/.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) userTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FBR Tax Application - Login");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}