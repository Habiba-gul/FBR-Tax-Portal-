package Frontend;

import Backend.PaymentHistory;
import Backend.PaymentHistoryDAO;
import Backend.SystemManager;
import Backend.UserInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UserDashboardController {

    @FXML
    private void handleUserInfo(MouseEvent event) {
        switchScene(event, "UserInfo.fxml", "FBR Tax Portal - User Information", 800, 600);
    }

    @FXML
    private void handleCurrentTaxRecords(MouseEvent event) {
        switchScene(event, "UserTaxRates.fxml", "FBR Tax Portal - Current Tax Rates", 800, 600);
    }

    @FXML
    private void handleTaxCalculation(MouseEvent event) {
        switchScene(event, "TaxCalculation.fxml", "FBR Tax Portal - Tax Calculation", 800, 600);
    }

    @FXML
    private void handlePaymentTransaction(MouseEvent event) {
        switchScene(event, "PaymentTransaction.fxml", "FBR Tax Portal - Payment & Transaction", 800, 600);
    }

    @FXML
    private void handleTaxHistory(MouseEvent event) {
        switchScene(event, "TaxHistory.fxml", "FBR Tax Portal - Tax Payment History", 800, 600);
    }

    @FXML
    private void handleAlerts(MouseEvent event) {
        switchScene(event, "Notifications.fxml", "FBR Tax Portal - Alerts & Notifications", 800, 600);
    }

    @FXML
    private void handleReportReadyData(MouseEvent event) {
        UserInfo currentUser = SystemManager.getCurrentUser();
        if (currentUser == null) {
            new Alert(Alert.AlertType.ERROR, "No user logged in. Please login again.").show();
            switchScene(event, "login.fxml", "FBR Tax Portal - Login", 800, 600);
            return;
        }

        try {
            int userId = currentUser.getId();
            List<PaymentHistory> allHistory = new PaymentHistoryDAO().getHistoryByUser(userId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportView.fxml"));
            Parent root = loader.load();

            ReportViewController controller = loader.getController();
            controller.setHistory(allHistory);

            // Use the normal switchScene that takes an already-loaded root
            switchScene(event, root, "FBR Tax Portal - Report-Ready Data", 800, 600);

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Report-Ready Data.").show();
        }
    }

    @FXML
    private void handleLogout(MouseEvent event) {
        SystemManager.logout();
        switchScene(event, "login.fxml", "FBR Tax Portal - Login", 800, 600);
    }

    // This version is used by most buttons (loads FXML first)
    private void switchScene(MouseEvent event, String fxmlPath, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load " + fxmlPath).show();
        }
    }

    // This version is used when the root is already loaded (Report-Ready Data)
    private void switchScene(MouseEvent event, Parent root, String title, int width, int height) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.centerOnScreen();
        stage.show();
    }
}