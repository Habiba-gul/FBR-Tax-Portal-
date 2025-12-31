package Frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import Backend.SystemManager;

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

    @FXML
    private void handlePaymentAndTransaction(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentTransaction.fxml"));
            Parent root = loader.load();
            PaymentTransactionController ctrl = loader.getController();

            // Get the calculated tax from SystemManager
            double totalTax = SystemManager.getTotalTax();
            double percentage = 17.0;  // You can make this dynamic later
            double deducted = totalTax * 0.1;  // Example deduction

            ctrl.setTaxDetails(totalTax, percentage, deducted);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Payment & Transaction Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHistoryAndRecords(MouseEvent event) {
        System.out.println("History & Records Clicked");
        // Add your history screen later
    }

    @FXML
    private void handleAlertsAndNotifications(MouseEvent event) {
        System.out.println("Alerts & Notifications Clicked");
        // Add alerts screen later
    }

    @FXML
    private void handleReportReadyData(MouseEvent event) {
        System.out.println("Report Ready Data Clicked");
        // Add report screen later
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));  // Note: small 'l' as per your file name
            Parent root = loader.load();

            Stage stage = (Stage) sourceNode.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);

            stage.setScene(scene);
            stage.setTitle("FBR Tax Portal - Login");
            stage.show();

            // Clear session
            SystemManager.logout();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load login.fxml during logout.");
        }
    }

    /**
     * Helper method to reduce code repetition when switching scenes.
     */
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
}