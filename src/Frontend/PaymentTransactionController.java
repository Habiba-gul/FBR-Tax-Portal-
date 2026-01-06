package Frontend;

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
import java.util.List;

public class PaymentTransactionController {

    @FXML private Label totalTaxLabel;
    @FXML private Label taxPercentageLabel;
    @FXML private Label taxDeductedLabel;

    @FXML private TableView<ReceiptItem> receiptTable;
    @FXML private TableColumn<ReceiptItem, String> descCol;
    @FXML private TableColumn<ReceiptItem, Double> priceCol;
    @FXML private TableColumn<ReceiptItem, Integer> qtyCol;
    @FXML private TableColumn<ReceiptItem, Double> taxCol;

    private final PaymentHistoryDAO historyDAO = new PaymentHistoryDAO();

    @FXML
    public void initialize() {
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        taxCol.setCellValueFactory(new PropertyValueFactory<>("tax"));

        double totalTax = SystemManager.getTotalTax();
        List<ReceiptItem> items = SystemManager.getReceiptItems();

        receiptTable.setItems(FXCollections.observableArrayList(items));
        totalTaxLabel.setText(String.format("Total Tax Amount: %.2f PKR", totalTax));
        taxPercentageLabel.setText("Tax Percentage: Varies by Category");  // Placeholder, can calculate avg if needed
        taxDeductedLabel.setText(String.format("Tax Deducted: %.2f PKR", totalTax));  // Assuming full deduction
    }

    @FXML
    private void handlePay(ActionEvent event) {
        double totalTax = SystemManager.getTotalTax();
        if (totalTax <= 0) {
            new Alert(Alert.AlertType.WARNING, "No tax to pay.").show();
            return;
        }

        // Build details string: "Desc:Price:Qty:Tax; Desc:Price:Qty:Tax; ..."
        StringBuilder detailsBuilder = new StringBuilder();
        for (ReceiptItem item : SystemManager.getReceiptItems()) {
            detailsBuilder.append(item.getDescription()).append(":")
                    .append(item.getPrice()).append(":")
                    .append(item.getQuantity()).append(":")
                    .append(item.getTax()).append(";");
        }
        String details = detailsBuilder.toString();

        // Insert to history
        int userId = SystemManager.getCurrentUser().getId();
        boolean success = historyDAO.insertPayment(userId, totalTax, details);

        if (success) {
            new Alert(Alert.AlertType.INFORMATION, String.format("Payment of %.2f PKR successful! Recorded in history.", totalTax)).show();
            SystemManager.clearReceipt();
        } else {
            new Alert(Alert.AlertType.ERROR, "Payment failed to record. Try again.").show();
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