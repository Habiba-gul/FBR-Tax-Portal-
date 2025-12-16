package Frontend;

import Backend.User;
import Backend.AdminService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
            }
        });
    }

    private void updateControlPanel(User user) {
        selectedNameLabel.setText("Name: " + user.getName());
        selectedCnicLabel.setText("CNIC: " + user.getCnic());
        selectedStatusLabel.setText("Status: " + user.getStatus());
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
        }
    }

    @FXML
    private void handlePenalty() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // Create a confirmation pop-up
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Penalty");
        alert.setHeaderText("Issue 10% Penalty to " + selected.getName() + "?");
        alert.setContentText("This action will add " + (selected.getBaseTax() * 0.10) + " to their record.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            adminService.applyTenPercentPenalty(selected);
            userTable.refresh();
            System.out.println("Penalty applied to " + selected.getCnic());
        }
    }

    @FXML private void handleSuspend() { 
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Suspended");
            userTable.refresh();
            updateControlPanel(selected);
        }
    }

    @FXML private void handleLogout(ActionEvent event) { /* Navigation logic */ }
    @FXML private void handleTaxSettings() { /* Navigation logic */ }
}