package Frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class PaymentTransactionController {

    @FXML private Label totalTaxLabel;
    @FXML private Label taxPercentageLabel;
    @FXML private Label taxDeductedLabel;

    // Call this when opening the screen (e.g., from TaxCalculationController)
    public void setTaxDetails(double totalTax, double percentage, double deducted) {
        totalTaxLabel.setText(String.format("Total Tax Amount: %.2f PKR", totalTax));
        taxPercentageLabel.setText(String.format("Tax Percentage: %.2f%%", percentage));
        taxDeductedLabel.setText(String.format("Tax Deducted: %.2f PKR", deducted));
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