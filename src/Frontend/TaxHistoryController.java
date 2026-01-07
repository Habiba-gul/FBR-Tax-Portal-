package Frontend;

import Backend.PaymentHistory;
import Backend.PaymentHistoryDAO;
import Backend.SystemManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaxHistoryController {

    @FXML private TableView<PaymentHistory> historyTable;
    @FXML private TableColumn<PaymentHistory, Integer> idCol;
    @FXML private TableColumn<PaymentHistory, String> dateCol;
    @FXML private TableColumn<PaymentHistory, Double> totalCol;

    @FXML private TableView<ReceiptItem> detailsTable;
    @FXML private TableColumn<ReceiptItem, String> descDetailCol;
    @FXML private TableColumn<ReceiptItem, Double> priceDetailCol;
    @FXML private TableColumn<ReceiptItem, Integer> qtyDetailCol;
    @FXML private TableColumn<ReceiptItem, Double> taxDetailCol;

    @FXML private Button viewReportBtn;

    private PaymentHistoryDAO dao = new PaymentHistoryDAO();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
                p.getValue().getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalTax"));

        descDetailCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceDetailCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyDetailCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        taxDetailCol.setCellValueFactory(new PropertyValueFactory<>("tax"));

        loadHistory();

        historyTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            viewReportBtn.setDisable(newVal == null);
            if (newVal != null) {
                loadDetails(newVal);
            } else {
                detailsTable.getItems().clear();
            }
        });

        viewReportBtn.setDisable(true);
    }

    private void loadHistory() {
        int userId = SystemManager.getCurrentUser().getId();
        List<PaymentHistory> history = dao.getHistoryByUser(userId);
        historyTable.setItems(FXCollections.observableArrayList(history));
    }

    private void loadDetails(PaymentHistory history) {
        detailsTable.getItems().clear();
        String detailsStr = history.getDetails();
        if (detailsStr == null || detailsStr.isEmpty()) return;

        String[] rows = detailsStr.split("\\|");
        for (String row : rows) {
            if (row.trim().isEmpty()) continue;
            String[] parts = row.split(",");
            if (parts.length == 6) {  // New format: taxType,category,tax%,original,tax,penalty
                try {
                    String desc = parts[0] + " - " + parts[1]; // e.g., "Salary - Government"
                    double price = Double.parseDouble(parts[3].trim());
                    int qty = 1; // Quantity not used, set to 1
                    double tax = Double.parseDouble(parts[4].trim());
                    detailsTable.getItems().add(new ReceiptItem(desc, price, qty, tax));
                } catch (Exception ignored) {}
            }
        }
    }

    @FXML
private void handleViewReport(ActionEvent event) {
    try {
        List<PaymentHistory> allHistory = new PaymentHistoryDAO().getHistoryByUser(SystemManager.getCurrentUser().getId());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportView.fxml"));
        Parent root = loader.load();

        ReportViewController controller = loader.getController();
        controller.setHistory(allHistory);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("FBR Tax Portal - Tax Records");
        stage.centerOnScreen();
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
        showAlert("Error", "Failed to load report.");
    }
}

    @FXML
    private void handleBack(ActionEvent event) {
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
            showAlert("Error", "Failed to return to dashboard.");
        }
    }

    // Helper alert method (was missing — this fixes your error)
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}