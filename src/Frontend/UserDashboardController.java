package Frontend;

import Backend.PaymentHistory;
import Backend.PaymentHistoryDAO;
import Backend.SystemManager;
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

    /* ---------- USER INFO & STATUS ---------- */
    @FXML
    private void handleUserInfo(MouseEvent event) {
        switchScene(event, "UserInfo.fxml", "FBR Tax Portal - User Information", 800, 600);
    }

    /* ---------- CURRENT TAX RATES ---------- */
    @FXML
    private void handleCurrentTaxRecords(MouseEvent event) {
        switchScene(event, "UserTaxRates.fxml", "FBR Tax Portal - Current Tax Rates", 800, 600);
    }

    /* ---------- TAX CALCULATION ---------- */
    @FXML
    private void handleTaxCalculation(MouseEvent event) {
        switchScene(event, "TaxCalculation.fxml", "FBR Tax Portal - Tax Calculation", 800, 600);
    }

    /* ---------- PAYMENT & TRANSACTION ---------- */
    @FXML
    private void handlePaymentTransaction(MouseEvent event) {
        switchScene(event, "PaymentTransaction.fxml", "FBR Tax Portal - Payment & Transaction", 800, 600);
    }

    /* ---------- HISTORY & RECORDS RETRIEVAL ---------- */
    @FXML
    private void handleTaxHistory(MouseEvent event) {          // ← CHANGED NAME TO MATCH FXML
        switchScene(event, "TaxHistory.fxml", "FBR Tax Portal - Tax Payment History", 800, 600);
    }

    /* ---------- ALERTS & NOTIFICATIONS ---------- */
    @FXML
    private void handleAlerts(MouseEvent event) {
        switchScene(event, "Notifications.fxml", "FBR Tax Portal - Alerts & Notifications", 800, 600);
    }

    /* ---------- REPORT-READY DATA (NEW LAYOUT) ---------- */
    @FXML
    private void handleReportReadyData(MouseEvent event) {      // ← Fixed name (was handleReportReady in earlier version)
        try {
            int userId = SystemManager.getCurrentUser().getId();
            List<PaymentHistory> allHistory = new PaymentHistoryDAO().getHistoryByUser(userId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportView.fxml"));
            Parent root = loader.load();

            ReportViewController controller = loader.getController();
            controller.setHistory(allHistory);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.setTitle("FBR Tax Portal - Report-Ready Data");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Report-Ready Data.").showAndWait();
        }
    }

    /* ---------- LOGOUT ---------- */
    @FXML
    private void handleLogout(MouseEvent event) {
        SystemManager.logout();
        switchScene(event, "login.fxml", "FBR Tax Portal - Login", 800, 600);
    }

    /* ---------- HELPER TO SWITCH SCENES ---------- */
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
            new Alert(Alert.AlertType.ERROR, "Failed to load " + fxmlPath).showAndWait();
        }
    }
}