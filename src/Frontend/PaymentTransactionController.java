package Frontend;

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

import Backend.SystemManager;

import java.io.IOException;

public class PaymentTransactionController {

    @FXML private Label totalTaxLabel;
    @FXML private Label taxPercentageLabel;
    @FXML private Label taxDeductedLabel;

    @FXML private TableView<ReceiptItem> receiptTable;
    @FXML private TableColumn<ReceiptItem, String> descCol;
    @FXML private TableColumn<ReceiptItem, Double> priceCol;
    @FXML private TableColumn<ReceiptItem, Integer> qtyCol;
    @FXML private TableColumn<ReceiptItem, Double> taxCol;

    @FXML
    public void initialize() {
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        taxCol.setCellValueFactory(new PropertyValueFactory<>("tax"));

        double totalTax = SystemManager.getTotalTax();
        double percentage = 17.0;
        double deducted = totalTax * 0.1;

        totalTaxLabel.setText(String.format("Total Tax Amount: %.2f PKR", totalTax));
        taxPercentageLabel.setText(String.format("Tax Percentage: %.2f%%", percentage));
        taxDeductedLabel.setText(String.format("Tax Deducted: %.2f PKR", deducted));

        receiptTable.setItems(FXCollections.observableArrayList(SystemManager.getReceiptItems()));
    }

    // THIS METHOD WAS MISSING — ADD IT
    public void setTaxDetails(double totalTax, double percentage, double deducted) {
        totalTaxLabel.setText(String.format("Total Tax Amount: %.2f PKR", totalTax));
        taxPercentageLabel.setText(String.format("Tax Percentage: %.2f%%", percentage));
        taxDeductedLabel.setText(String.format("Tax Deducted: %.2f PKR", deducted));
    }

    @FXML
    private void payTax(ActionEvent event) {
        double totalTax = SystemManager.getTotalTax();
        if (totalTax <= 0) {
            new Alert(Alert.AlertType.WARNING, "No tax to pay.").show();
            return;
        }

        new Alert(Alert.AlertType.INFORMATION, String.format("Payment of %.2f PKR successful!", totalTax)).show();
        SystemManager.clearReceipt();
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