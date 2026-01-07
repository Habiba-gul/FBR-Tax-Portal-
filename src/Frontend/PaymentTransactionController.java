package Frontend;

import Backend.PaymentHistoryDAO;
import Backend.SystemManager;
import Backend.NotificationDAO;
import Backend.UserDAO;
import Backend.UserInfo;
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
import java.sql.Date;
import java.time.LocalDate;

public class PaymentTransactionController {

    @FXML private Label totalTaxLabel;
    @FXML private Label taxPercentageLabel;
    @FXML private Label taxDeductedLabel;

    @FXML private TableView<ReceiptItem> receiptTable;
    @FXML private TableColumn<ReceiptItem, String> descCol;
    @FXML private TableColumn<ReceiptItem, Double> priceCol;
    @FXML private TableColumn<ReceiptItem, Integer> qtyCol;
    @FXML private TableColumn<ReceiptItem, Double> taxCol;

    private PaymentHistoryDAO dao = new PaymentHistoryDAO();
    private double totalTax;

    @FXML
    public void initialize() {
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        taxCol.setCellValueFactory(new PropertyValueFactory<>("tax"));

        loadReceipt();
    }

    private void loadReceipt() {
        totalTax = SystemManager.getTotalTax();
        receiptTable.setItems(FXCollections.observableArrayList(SystemManager.getReceiptItems()));

        totalTaxLabel.setText(String.format("Total Tax Amount: %.2f PKR", totalTax));
        taxPercentageLabel.setText("Tax Percentage: Dynamic");
        taxDeductedLabel.setText(String.format("Tax Deducted: %.2f PKR", totalTax));
    }

    @FXML
    private void handlePay(ActionEvent event) {
        UserInfo currentUser = SystemManager.getCurrentUser();
        if (currentUser == null) {
            new Alert(Alert.AlertType.ERROR, "No user logged in. Please login again.").showAndWait();
            return;
        }

        int userId = currentUser.getId();

        StringBuilder detailsBuilder = new StringBuilder();

        for (ReceiptItem item : receiptTable.getItems()) {
            String description = item.getDescription();

            String taxType = extractTaxType(description);
            String category = extractCategory(description);

            double taxPercent = item.getPrice() > 0 ? (item.getTax() / item.getPrice()) * 100 : 0.0;

            double originalPrice = item.getPrice();
            double taxAmount = item.getTax();
            double penalty = 0.0;

            detailsBuilder.append(taxType).append(",")
                          .append(category).append(",")
                          .append(String.format("%.2f", taxPercent)).append(",")
                          .append(String.format("%.2f", originalPrice)).append(",")
                          .append(String.format("%.2f", taxAmount)).append(",")
                          .append(String.format("%.2f", penalty)).append("|");
        }

        String details = detailsBuilder.toString();

        boolean success = dao.insertPayment(userId, totalTax, details);

        if (success) {
            UserDAO.updateTaxStatus(userId, "Paid");
            UserDAO.updatePaymentDate(userId, Date.valueOf(LocalDate.now()));

            String message = "Dear " + currentUser.getName() + ",\n\n" +
                    "Your tax payment of PKR " + String.format("%.2f", totalTax) +
                    " has been successfully received on " + LocalDate.now() + ".\n\n" +
                    "Thank you for fulfilling your tax obligations.\n" +
                    "Your taxpayer status is now 'Paid'.\n\n" +
                    "For queries: FBR Helpline 111-772-772\n\n" +
                    "Federal Board of Revenue\nGovernment of Pakistan";

            NotificationDAO.addNotification(userId, "Payment Confirmation", message, "PAYMENT");

            new Alert(Alert.AlertType.INFORMATION,
                    String.format("Payment of %.2f PKR successful! Recorded in history.", totalTax)).showAndWait();

            SystemManager.clearReceipt();
            loadReceipt(); // Clear table
        } else {
            new Alert(Alert.AlertType.ERROR, "Payment failed to record. Please try again.").showAndWait();
        }
    }

    private String extractTaxType(String desc) {
        String lower = desc.toLowerCase();
        if (lower.contains("salary")) return "Salary";
        if (lower.contains("property")) return "Property";
        if (lower.contains("vehicle")) return "Vehicle";
        if (lower.contains("gst")) return "GST";
        return "Other";
    }

    private String extractCategory(String desc) {
        if (desc.contains("Government")) return "Government";
        if (desc.contains("Private")) return "Private Company";
        if (desc.contains("Business")) return "Business";
        if (desc.contains("Residential")) return "Residential";
        if (desc.contains("Commercial")) return "Commercial";
        if (desc.contains("Below 1000cc")) return "Below 1000cc";
        if (desc.contains("Above 1000cc")) return "Above 1000cc";
        return desc;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("FBR Tax Portal - User Dashboard");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}