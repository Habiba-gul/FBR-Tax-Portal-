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
    @FXML private Label taxDeductedLabel;
    @FXML private Label penaltyLabel;

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

    // Fetch penalty from DB
    double penalty = 0.0;
    UserInfo currentUser = SystemManager.getCurrentUser();
    if (currentUser != null) {
        penalty = UserDAO.getPenalty(currentUser.getId());
    }

    receiptTable.setItems(FXCollections.observableArrayList(SystemManager.getReceiptItems()));

    totalTaxLabel.setText(String.format("Total Tax Amount: %.2f PKR", totalTax + penalty));
    taxDeductedLabel.setText(String.format("Tax Deducted: %.2f PKR", totalTax));

    // Show penalty only if > 0
    if (penalty > 0) {
        penaltyLabel.setText(String.format("Penalty: %.2f PKR", penalty));
        penaltyLabel.setVisible(true);
    } else {
        penaltyLabel.setText("Penalty: 0.00 PKR");
        penaltyLabel.setVisible(false);  // Hide when zero
    }
}

    @FXML
    private void handlePay(ActionEvent event) {
        UserInfo currentUser = SystemManager.getCurrentUser();
        if (currentUser == null) {
            new Alert(Alert.AlertType.ERROR, "No user logged in. Please login again.").showAndWait();
            return;
        }

        int userId = currentUser.getId();

        // Fetch current penalty
        double penalty = UserDAO.getPenalty(userId);

        StringBuilder detailsBuilder = new StringBuilder();

        for (ReceiptItem item : receiptTable.getItems()) {
            String description = item.getDescription();
            String taxType = extractTaxType(description);
            String category = extractCategory(description);
            double taxPercent = item.getPrice() > 0 ? (item.getTax() / item.getPrice()) * 100 : 0.0;
            double originalPrice = item.getPrice();
            double taxAmount = item.getTax();

            detailsBuilder.append(taxType).append(",")
                          .append(category).append(",")
                          .append(String.format("%.2f", taxPercent)).append(",")
                          .append(String.format("%.2f", originalPrice)).append(",")
                          .append(String.format("%.2f", taxAmount)).append(",")
                          .append("0|");
        }

        // Add penalty if exists
        if (penalty > 0) {
            detailsBuilder.append("Penalty,Overdue Penalty,0.0,0.0,")
                          .append(String.format("%.2f", penalty))
                          .append(",0|");
            totalTax += penalty;

            // Clear penalty after payment
            UserDAO.updatePenalty(userId, 0.0);
        }

        String details = detailsBuilder.toString();

        boolean success = dao.insertPayment(userId, totalTax, details);

        if (success) {
            UserDAO.updateTaxStatus(userId, "Paid");
            UserDAO.updatePaymentDate(userId, Date.valueOf(LocalDate.now()));

            String message = "Dear " + currentUser.getName() + ",\n\n" +
                    "Your tax payment of PKR " + String.format("%.2f", totalTax) +
                    " has been successfully received on " + LocalDate.now() + ".\n\n" +
                    (penalty > 0 ? "This includes clearance of your overdue penalty of PKR " + String.format("%.2f", penalty) + ".\n\n" : "") +
                    "Thank you for your compliance.\n\nFBR";

            NotificationDAO.addNotification(userId, "Payment Confirmation", message, "PAYMENT");

            new Alert(Alert.AlertType.INFORMATION,
                    String.format("Payment of %.2f PKR successful! Recorded in history.", totalTax)).showAndWait();

            SystemManager.clearReceipt();
            loadReceipt();
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