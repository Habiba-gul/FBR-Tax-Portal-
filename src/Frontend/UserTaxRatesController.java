package Frontend;

import java.io.IOException;

import Backend.TaxRateService;
import Backend.TaxSlab;
import Backend.TaxSlabService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UserTaxRatesController {

    @FXML private TableView<TaxSlab> taxTable;
    @FXML private TableColumn<TaxSlab, String> typeCol;
    @FXML private TableColumn<TaxSlab, String> categoryCol;
    @FXML private TableColumn<TaxSlab, Double> minCol;
    @FXML private TableColumn<TaxSlab, Double> maxCol;
    @FXML private TableColumn<TaxSlab, Double> rateCol;
    @FXML private Button backBtn;

    private final TaxSlabService slabService = new TaxSlabService();
    private final TaxRateService taxRateService = TaxRateService.getInstance();

    @FXML
    public void initialize() {
        setupTableColumns();
        refreshTaxTable();
    }

    private void setupTableColumns() {
        typeCol.setCellValueFactory(new PropertyValueFactory<>("taxType"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        minCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
    }

    private void refreshTaxTable() {
        // Clear existing data
        taxTable.getItems().clear();
        
        // Get updated tax rates from TaxRateService
        double govtJobRate = taxRateService.getGovtJobRate();
        double privateJobRate = taxRateService.getPrivateJobRate();
        double businessRate = taxRateService.getBusinessRate(); // Get the new business rate
        double vehicleRate = taxRateService.getVehicleRate();
        double residentialRate = taxRateService.getResidentialRate();
        double commercialRate = taxRateService.getCommercialRate();
        double gstRate = taxRateService.getGstRate();
        
        // Create updated tax slabs based on current rates
        taxTable.getItems().addAll(
            new TaxSlab("Income Tax", "Govt Job", 0, 600000, govtJobRate),
            new TaxSlab("Income Tax", "Private Job", 0, 600000, privateJobRate),
            new TaxSlab("Income Tax", "Business", 0, 1000000, businessRate), // Added new row for Business
            new TaxSlab("Vehicle Tax", "All Vehicles", 0, 1000000, vehicleRate),
            new TaxSlab("Property Tax", "Residential", 0, 500000, residentialRate),
            new TaxSlab("Property Tax", "Commercial", 0, 1000000, commercialRate),
            new TaxSlab("GST", "All Categories", 0, 10000000, gstRate)
        );
        
        taxTable.setEditable(false); // USER READ ONLY
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}