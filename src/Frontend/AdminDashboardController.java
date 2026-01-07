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

    private final AdminService adminService = new AdminService();
    private ObservableList<User> users;

    @FXML
    public void initialize() {
        cnicColumn.setCellValueFactory(new PropertyValueFactory<>("cnic"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        penaltyColumn.setCellValueFactory(new PropertyValueFactory<>("penalty"));

        loadUsers();
    }

    private void loadUsers() {
        users = adminService.getAllUsers();
        userTable.setItems(users);
    }

    @FXML
    private void handleTaxRates() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("TaxRateSettings.fxml"));
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("FBR Tax Portal - Tax Rate Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdatePenalty() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getPenalty()));
        dialog.setTitle("Update Penalty");
        dialog.setHeaderText("Enter new penalty amount for " + selected.getName());
        dialog.setContentText("Penalty:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(penaltyStr -> {
            try {
                double newPenalty = Double.parseDouble(penaltyStr);
                if (adminService.updatePenalty(selected.getId(), newPenalty)) {
                    selected.setPenalty(newPenalty);
                    userTable.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Penalty updated!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update penalty.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
            }
        });
    }

    @FXML
    private void handleSendReminder() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user.");
            return;
        }

        if ("Paid".equals(selected.getStatus())) {
            showAlert(Alert.AlertType.WARNING, "Invalid Action", "User is already paid.");
            return;
        }

        adminService.sendTaxReminder(selected.getId());
        showAlert(Alert.AlertType.INFORMATION, "Success", "Reminder sent to " + selected.getName() + "!");
    }

    @FXML
    private void handleRefresh() {
        loadUsers();
    }

    @FXML
    private void handleToggleStatus() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user.");
            return;
        }

        String currentStatus = selected.getStatus();
        String newStatus = "Paid".equals(currentStatus) ? "Unpaid" : "Paid";

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Status Change");
        confirm.setHeaderText("Change status for " + selected.getName() + " from " + currentStatus + " to " + newStatus + "?");
        confirm.setContentText("This action will update the user's tax status.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (adminService.updateStatus(selected.getId(), newStatus)) {
                selected.setStatus(newStatus);
                userTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Status updated to " + newStatus + "!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update status.");
            }
        }
    }

    @FXML private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("FBR Tax Portal - Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}