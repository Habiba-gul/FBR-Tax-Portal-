package Frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TaxRateSettingsController {

    @FXML
    private void handleSalarySettings(ActionEvent event) {
        openSubSettings(event, "SalaryTaxSettings.fxml", "Manage Salary Tax Ranges");
    }

    @FXML
    private void handlePropertySettings(ActionEvent event) {
        openSubSettings(event, "PropertyTaxSettings.fxml", "Manage Property Tax Ranges");
    }

    @FXML
    private void handleVehicleSettings(ActionEvent event) {
        openSubSettings(event, "VehicleTaxSettings.fxml", "Manage Vehicle Tax Ranges");
    }

    @FXML
    private void handleGstSettings(ActionEvent event) {
        openSubSettings(event, "GstTaxSettings.fxml", "Manage GST Ranges");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSubSettings(ActionEvent event, String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}