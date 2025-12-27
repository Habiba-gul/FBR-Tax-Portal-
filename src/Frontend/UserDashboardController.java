package Frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDashboardController {

    // --- Dashboard Function Handlers ---

    @FXML
    private void handleUserInfo(MouseEvent event) {
        switchScene(event, "UserInfo.fxml", "FBR Tax Portal - User Information", 800, 600);
    }

    @FXML
    private void handleCurrentTaxRecords(MouseEvent event) {
        switchScene(event, "UserTaxRates.fxml", "FBR Tax Portal - Tax Rates", 900, 700);
    }

    @FXML
    private void handleTaxCalculationServices(MouseEvent event) {
        switchScene(event, "TaxCalculation.fxml", "FBR Tax Portal - Tax Calculation Services", 1000, 800);
    }

    private void switchScene(MouseEvent event, String fxmlPath, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, width, height);

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load " + fxmlPath);
            e.printStackTrace();
        }
    }

    // --- Logout Functionality ---

    @FXML
    private void handleLogout(ActionEvent event) {
        returnToLogin((Node) event.getSource());
    }

    @FXML
    private void handleLogout(MouseEvent event) {
        returnToLogin((Node) event.getSource());
    }

    private void returnToLogin(Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) sourceNode.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);

            stage.setScene(scene);
            stage.setTitle("FBR Tax Application - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Login.fxml during logout.");
        }
    }

    // Placeholder handlers for remaining features
    @FXML
private void handlePaymentAndTransaction(MouseEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentTransaction.fxml"));
        Parent root = loader.load();
        PaymentTransactionController ctrl = loader.getController();

        // Example data - replace with real calculated values from TaxCalculationController
        double totalTax = 50000.0;  // Get from your calculation
        double percentage = 17.0;
        double deducted = totalTax * 0.1;

        ctrl.setTaxDetails(totalTax, percentage, deducted);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Payment & Transaction Details");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @FXML private void handleHistoryAndRecords(MouseEvent event) { System.out.println("History Clicked"); }
    @FXML private void handleAlertsAndNotifications(MouseEvent event) { System.out.println("Alerts Clicked"); }
    @FXML private void handleReportReadyData(MouseEvent event) { System.out.println("Reports Clicked"); }
}