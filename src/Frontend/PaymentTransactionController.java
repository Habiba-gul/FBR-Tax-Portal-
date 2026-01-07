package Frontend;

import Backend.PaymentHistoryDAO;
import Backend.SystemManager;
import Backend.NotificationDAO;
import Backend.UserDAO;
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
        List<ReceiptItem> items = SystemManager.getReceiptItems();
        totalTax = SystemManager.getTotalTax();
        receiptTable.setItems(FXCollections.observableArrayList(items));
        totalTaxLabel.setText(String.format("Total Tax Amount: %.2f PKR", totalTax));
        taxPercentageLabel.setText("Tax Percentage: 17%"); // Adjust if dynamic
        taxDeductedLabel.setText(String.format("Tax Deducted: %.2f PKR", totalTax));
    }

    @FXML
    private void handlePay(ActionEvent event) {
        int userId = SystemManager.getCurrentUser().getId();
        StringBuilder details = new StringBuilder();
        for (ReceiptItem item : receiptTable.getItems()) {
            details.append(item.getDescription()).append(",")
                   .append(item.getPrice()).append(",")
                   .append(item.getQuantity()).append(",")
                   .append(item.getTax()).append("|");
        }
        boolean success = dao.insertPayment(userId, totalTax, details.toString());
        if (success) {
            // Update status to Paid
            UserDAO.updateTaxStatus(userId, "Paid");

            // Update payment date
            Date sqlDate = Date.valueOf(LocalDate.now());
            UserDAO.updatePaymentDate(userId, sqlDate);

            // Send payment confirmation notification (real, official)
            String msg = "Dear " + SystemManager.getCurrentUser().getName() + ",\n\n" +
                         "This is to confirm that your tax payment of " + String.format("%.2f", totalTax) + " PKR has been successfully recorded on " + LocalDate.now() + ".\n\n" +
                         "Thank you for your compliance with FBR regulations.\n\n" +
                         "Your status has been updated to Paid. For any queries, contact FBR Helpline at 111-772-772.\n\n" +
                         "Regards,\n" +
                         "Federal Board of Revenue (FBR)\n" +
                         "Government of Pakistan";
            NotificationDAO.addNotification(userId, "Tax Payment Confirmation", msg, "PAYMENT");

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