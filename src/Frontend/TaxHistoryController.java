package Frontend;

import Backend.PaymentHistory;
import Backend.PaymentHistoryDAO;
import Backend.SystemManager;
import javafx.beans.property.SimpleStringProperty;
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

    private PaymentHistoryDAO dao = new PaymentHistoryDAO();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalTax"));

        descDetailCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceDetailCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyDetailCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        taxDetailCol.setCellValueFactory(new PropertyValueFactory<>("tax"));

        loadHistory();

        // Listener for row selection to load details
        historyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadDetails(newSelection);
            }
        });
    }

    private void loadHistory() {
        int userId = SystemManager.getCurrentUser().getId();
        List<PaymentHistory> history = dao.getHistoryByUser(userId);
        historyTable.setItems(FXCollections.observableArrayList(history));
        System.out.println("Loaded " + history.size() + " history entries"); // Debug
    }

    private void loadDetails(PaymentHistory history) {
        detailsTable.getItems().clear();
        String details = history.getDetails();
        System.out.println("Loading details for ID " + history.getId() + ": " + details); // Debug
        if (details != null && !details.isEmpty()) {
            String[] items = details.split("\\|");
            for (String item : items) {
                if (item.trim().isEmpty()) continue;
                String[] parts = item.split(",");
                if (parts.length == 4) {
                    try {
                        String desc = parts[0].trim();
                        double price = Double.parseDouble(parts[1].trim());
                        int qty = Integer.parseInt(parts[2].trim());
                        double tax = Double.parseDouble(parts[3].trim());
                        detailsTable.getItems().add(new ReceiptItem(desc, price, qty, tax));
                        System.out.println("Added detail: " + desc + ", tax=" + tax); // Debug
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing detail: " + item); // Debug error
                    }
                }
            }
        } else {
            System.out.println("No details available for this payment."); // Debug
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("FBR Tax Portal - Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}