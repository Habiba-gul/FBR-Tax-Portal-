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

    // --- Dashboard Function Handlers (Called by VBox onClicked) ---

    @FXML
    private void handleUserInfo(MouseEvent event) {
        System.out.println("Navigating to User Information and Status screen...");
    }

    @FXML
    private void handleCurrentTaxRecords(MouseEvent event) {
        System.out.println("Navigating to Current Tax Records screen...");
    }

    @FXML
    private void handleTaxCalculationServices(MouseEvent event) {
        System.out.println("Navigating to Tax Calculation Services Hub (Income Tax)...");
        try {
            // 1. Load the FXML file for the Income Tax Form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IncomeTaxForm.fxml"));
            Parent root = loader.load();

            // 2. Get the current Stage (window) from the click event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // 3. Create a new Scene (Larger size for the form)
            Scene scene = new Scene(root, 1000, 750); 
            
            // 4. Set the new Scene
            stage.setScene(scene);
            stage.setTitle("FBR Tax Portal - Income Tax Form");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load IncomeTaxForm.fxml");
        }
    }

    @FXML
    private void handlePaymentAndTransaction(MouseEvent event) {
        System.out.println("Navigating to Payment and Transaction screen...");
    }

    @FXML
    private void handleHistoryAndRecords(MouseEvent event) {
        System.out.println("Navigating to History and Records Retrieval screen...");
    }

    @FXML
    private void handleAlertsAndNotifications(MouseEvent event) {
        System.out.println("Navigating to Alerts and Notification screen...");
    }

    @FXML
    private void handleReportReadyData(MouseEvent event) {
        System.out.println("Navigating to Report Ready Data screen...");
    }

    // --- Logout Functionality ---

    @FXML
    private void handleLogout(ActionEvent event) {
        // Called by the header button
        returnToLogin((Node) event.getSource());
    }

    @FXML
    private void handleLogout(MouseEvent event) {
        // Called by the VBox grid item
        returnToLogin((Node) event.getSource());
    }

    private void returnToLogin(Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) sourceNode.getScene().getWindow();
            // Revert back to the original login size
            Scene scene = new Scene(root, 800, 600); 
            
            stage.setScene(scene);
            stage.setTitle("FBR Tax Application - Login");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Login.fxml during logout.");
        }
    }
}