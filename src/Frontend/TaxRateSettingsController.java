package Frontend;

import Backend.TaxRateService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TaxRateSettingsController {

    @FXML private TextField salaryRateField;
    @FXML private TextField businessRateField;
    @FXML private TextField rentalRateField;
    @FXML private TextField capitalGainsRateField;
    @FXML private TextField penaltyRateField;

    private TaxRateService taxRateService = TaxRateService.getInstance();

    @FXML
    public void initialize() {
        // Load current rates into fields
        salaryRateField.setText(String.valueOf(taxRateService.getSalaryRate()));
        businessRateField.setText(String.valueOf(taxRateService.getBusinessRate()));
        rentalRateField.setText(String.valueOf(taxRateService.getRentalRate()));
        capitalGainsRateField.setText(String.valueOf(taxRateService.getCapitalGainsRate()));
        penaltyRateField.setText(String.valueOf(taxRateService.getPenaltyRate()));
    }

    @FXML
    private void handleSave() {
        try {
            double salary = Double.parseDouble(salaryRateField.getText());
            double business = Double.parseDouble(businessRateField.getText());
            double rental = Double.parseDouble(rentalRateField.getText());
            double capital = Double.parseDouble(capitalGainsRateField.getText());
            double penalty = Double.parseDouble(penaltyRateField.getText());

            // Validate reasonable ranges (0% to 50%)
            if (salary < 0 || salary > 50 || business < 0 || business > 50 ||
                rental < 0 || rental > 50 || capital < 0 || capital > 50 ||
                penalty < 0 || penalty > 100) {
                showAlert("Invalid Input", "Tax rates should be between 0 and 50% (penalty up to 100%).");
                return;
            }

            // Save new rates
            taxRateService.setSalaryRate(salary);
            taxRateService.setBusinessRate(business);
            taxRateService.setRentalRate(rental);
            taxRateService.setCapitalGainsRate(capital);
            taxRateService.setPenaltyRate(penalty);

            showAlert("Success", "Tax rates updated successfully!");

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for all rates.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            // Relative path to AdminDashboard.fxml (same package as this controller)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) salaryRateField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FBR Admin Portal");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load Admin Dashboard.\nCheck console for details.\n\nQuick Fix:\n1. Ensure AdminDashboard.fxml is in resources/Frontend/\n2. Update launch.json as per guide\n3. Clean and run again.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}