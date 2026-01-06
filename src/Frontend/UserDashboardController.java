package Frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
        switchScene(event, "TaxCalculation.fxml", "FBR Tax Portal - Tax Calculation", 900, 700);
    }

    @FXML
    private void handlePaymentTransactions(MouseEvent event) {
        switchScene(event, "PaymentTransaction.fxml", "FBR Tax Portal - Payment & Transactions", 900, 700);
    }

    @FXML
    private void handleTaxFiling(MouseEvent event) {
        switchScene(event, "IncomeTaxForm.fxml", "FBR Tax Portal - Tax Filing", 1000, 750);
    }

    @FXML
    private void handleTaxReports(MouseEvent event) {
        switchScene(event, "TaxHistory.fxml", "FBR Tax Portal - Tax History & Reports", 800, 600);
    }

    @FXML
    private void handleTaxLaws(MouseEvent event) {
        // Placeholder: Add TaxLaws.fxml if needed
        switchScene(event, "TaxLaws.fxml", "FBR Tax Portal - Tax Laws", 800, 600);
    }
@FXML
private void handleAlerts(MouseEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("Notifications.fxml")
        );
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Alerts & Notifications");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
}


@FXML
private void handleReportReadyData(MouseEvent event) {
    if (SystemManager.getCurrentUser() != null) {
        int currentUserId = SystemManager.getCurrentUser().getId();
        // Fetch report-ready data for this user from DB (if implemented)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Ready Data");
        alert.setHeaderText("User ID: " + currentUserId);
        alert.setContentText("Report-Ready Data feature is under development.");
        alert.showAndWait();
    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("No user is currently logged in.");
        alert.showAndWait();
    }
}



    
    @FXML
    private void handleLogout(MouseEvent event) {
        try {
            SystemManager.logout();
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FBR Tax Portal - Login");
            stage.centerOnScreen();
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