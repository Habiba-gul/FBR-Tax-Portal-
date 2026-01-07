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
    @FXML private Label selectedPenaltyLabel;

    private AdminService service = new AdminService();
    private User selectedUser;

    @FXML
    public void initialize() {
        cnicColumn.setCellValueFactory(new PropertyValueFactory<>("cnic"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        penaltyColumn.setCellValueFactory(new PropertyValueFactory<>("penalty"));

        loadUsers();

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedUser = newSelection;
                selectedNameLabel.setText(selectedUser.getName());
                selectedCnicLabel.setText(selectedUser.getCnic());
                selectedStatusLabel.setText(selectedUser.getStatus());
                selectedPenaltyLabel.setText(String.valueOf(selectedUser.getPenalty()));
            }
        });
    }

    private void loadUsers() {
        ObservableList<User> users = service.getAllUsers();
        userTable.setItems(users);
    }

    @FXML
    private void handleUpdatePenalty() {
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No User Selected", "Please select a user.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedUser.getPenalty()));
        dialog.setTitle("Update Penalty");
        dialog.setHeaderText("Enter new penalty amount for " + selectedUser.getName());
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(penaltyStr -> {
            try {
                double penalty = Double.parseDouble(penaltyStr);
                service.updatePenalty(selectedUser.getCnic(), penalty);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Penalty updated.");
                loadUsers();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
            }
        });
    }

    @FXML
    private void handleSuspend() {
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No User Selected", "Please select a user.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Suspend User");
        confirm.setHeaderText("Suspend " + selectedUser.getName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            service.suspendUser(selectedUser.getCnic());
            showAlert(Alert.AlertType.INFORMATION, "Success", "User suspended.");
            loadUsers();
        }
    }

    @FXML
    private void handleOverride() {
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No User Selected", "Please select a user.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Override Status");
        confirm.setHeaderText("Set " + selectedUser.getName() + " status to Paid?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            service.updateStatus(selectedUser.getCnic(), "Paid");
            showAlert(Alert.AlertType.INFORMATION, "Success", "User status overridden to Paid.");
            loadUsers();
        }
    }

    @FXML
    private void handleSendReminder() {
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No User Selected", "Please select a user.");
            return;
        }
        // Functional call: Sends real notification
        service.sendReminder(selectedUser.getId(), selectedUser.getName());
        showAlert(Alert.AlertType.INFORMATION, "Success", "Official tax reminder sent to " + selectedUser.getName() + ". It will appear in their portal.");
    }

    @FXML
    private void handleRefresh() {
        loadUsers();
    }

    @FXML
    private void handleTaxRates() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("TaxRateSettings.fxml"));
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FBR Tax Portal - Tax Rates");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to open Tax Rate Settings.");
        }
    }

    @FXML private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root));
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