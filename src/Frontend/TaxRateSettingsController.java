package Frontend;

import Backend.TaxRateService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TaxRateSettingsController {

    @FXML private ComboBox<String> incomeTypeCombo;
    @FXML private TextField incomeTaxRateField;
    @FXML private TextField vehicleRateField;
    @FXML private TextField residentialRateField;
    @FXML private TextField commercialRateField;
    @FXML private TextField gstRateField;
    @FXML private TextField penaltyRateField;

    private TaxRateService taxRateService = TaxRateService.getInstance();

    @FXML
    public void initialize() {
        // Initialize income type combo box with "Business" added
        ObservableList<String> incomeTypes = FXCollections.observableArrayList("Govt Job", "Private Job", "Business");
        incomeTypeCombo.setItems(incomeTypes);
        incomeTypeCombo.getSelectionModel().selectFirst();
        
        // Load current rates for the selected income type
        loadCurrentIncomeTaxRate();
        
        // Load other rates
        vehicleRateField.setText(String.valueOf(taxRateService.getVehicleRate()));
        residentialRateField.setText(String.valueOf(taxRateService.getResidentialRate()));
        commercialRateField.setText(String.valueOf(taxRateService.getCommercialRate()));
        gstRateField.setText(String.valueOf(taxRateService.getGstRate()));
        penaltyRateField.setText(String.valueOf(taxRateService.getPenaltyRate()));
        
        // Add listener to update income tax rate field when selection changes
        incomeTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadCurrentIncomeTaxRate();
        });
    }
    
    private void loadCurrentIncomeTaxRate() {
        String selectedType = incomeTypeCombo.getSelectionModel().getSelectedItem();
        if (selectedType != null) {
            // Handle the new "Business" type
            if (selectedType.equals("Govt Job")) {
                incomeTaxRateField.setText(String.valueOf(taxRateService.getGovtJobRate()));
            } else if (selectedType.equals("Private Job")) {
                incomeTaxRateField.setText(String.valueOf(taxRateService.getPrivateJobRate()));
            } else if (selectedType.equals("Business")) {
                incomeTaxRateField.setText(String.valueOf(taxRateService.getBusinessRate()));
            }
        }
    }

    @FXML
    private void handleSave() {
        try {
            double incomeTax = Double.parseDouble(incomeTaxRateField.getText());
            double vehicle = Double.parseDouble(vehicleRateField.getText());
            double residential = Double.parseDouble(residentialRateField.getText());
            double commercial = Double.parseDouble(commercialRateField.getText());
            double gst = Double.parseDouble(gstRateField.getText());
            double penalty = Double.parseDouble(penaltyRateField.getText());

            // Validate reasonable ranges (0% to 50%)
            if (incomeTax < 0 || incomeTax > 50 || vehicle < 0 || vehicle > 50 ||
                residential < 0 || residential > 50 || commercial < 0 || commercial > 50 ||
                gst < 0 || gst > 50 || penalty < 0 || penalty > 100) {
                showAlert("Invalid Input", "Tax rates should be between 0 and 50% (penalty up to 100%).");
                return;
            }

            // Save new rates based on selected income type, including "Business"
            String selectedType = incomeTypeCombo.getSelectionModel().getSelectedItem();
            if (selectedType.equals("Govt Job")) {
                taxRateService.setGovtJobRate(incomeTax);
            } else if (selectedType.equals("Private Job")) {
                taxRateService.setPrivateJobRate(incomeTax);
            } else if (selectedType.equals("Business")) {
                taxRateService.setBusinessRate(incomeTax);
            }
            
            taxRateService.setVehicleRate(vehicle);
            taxRateService.setResidentialRate(residential);
            taxRateService.setCommercialRate(commercial);
            taxRateService.setGstRate(gst);
            taxRateService.setPenaltyRate(penalty);

            // Notify all observers that rates have changed
            taxRateService.notifyRateChange();
            
            showAlert("Success", "Tax rates updated successfully!");

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for all rates.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) incomeTaxRateField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FBR Admin Portal");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load Admin Dashboard.\nCheck console for details.");
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