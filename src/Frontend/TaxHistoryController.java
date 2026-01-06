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

    private final PaymentHistoryDAO historyDAO = new PaymentHistoryDAO();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getPaymentDate().format(formatter)));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalTax"));

        descDetailCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceDetailCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyDetailCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        taxDetailCol.setCellValueFactory(new PropertyValueFactory<>("tax"));

        // Load history
        int userId = SystemManager.getCurrentUser().getId();
        List<PaymentHistory> history = historyDAO.getHistoryByUser(userId);
        historyTable.setItems(FXCollections.observableArrayList(history));

        // On selection, show details
        historyTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                loadDetails(selected.getDetails());
            } else {
                detailsTable.getItems().clear();
            }
        });
    }

    private void loadDetails(String detailsStr) {
        detailsTable.getItems().clear();
        if (detailsStr == null || detailsStr.isEmpty()) return;

        // Parse details: "Desc:Price:Qty:Tax; Desc:Price:Qty:Tax; ..."
        String[] items = detailsStr.split(";");
        for (String item : items) {
            if (item.trim().isEmpty()) continue;
            String[] parts = item.split(":");
            if (parts.length == 4) {
                try {
                    String desc = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    int qty = Integer.parseInt(parts[2]);
                    double tax = Double.parseDouble(parts[3]);
                    detailsTable.getItems().add(new ReceiptItem(desc, price, qty, tax));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
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