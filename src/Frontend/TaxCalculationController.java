package Frontend;

import Backend.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaxCalculationController {

    @FXML private VBox dynamicInputPane;

    private final List<ReceiptItem> receiptItems = new ArrayList<>();

    @FXML
    public void initialize() {
        // No init needed - buttons are in FXML
    }

    @FXML
    private void addSalaryForm(ActionEvent event) {
        addForm("Salary");
    }

    @FXML
    private void addPropertyForm(ActionEvent event) {
        addForm("Property");
    }

    @FXML
    private void addVehiclesForm(ActionEvent event) {
        addForm("Vehicles");
    }

    @FXML
    private void addGstForm(ActionEvent event) {
        addForm("GST");
    }

    private void addForm(String category) {
        // Create category-specific form
        VBox form = new VBox(10);
        Label title = new Label("Details for " + category);
        form.getChildren().add(title);

        switch (category) {
            case "Salary":
                ComboBox<String> jobType = new ComboBox<>(FXCollections.observableArrayList("Government", "Private", "Business"));
                jobType.setPromptText("Job Type");
                TextField salaryField = new TextField();
                salaryField.setPromptText("Annual Salary (PKR)");
                form.getChildren().addAll(jobType, salaryField);
                form.setUserData(new SalaryTaxCalculator());  // Polymorphism
                break;
            case "Property":
                ComboBox<String> propType = new ComboBox<>(FXCollections.observableArrayList("Residential", "Commercial", "Both"));
                propType.setPromptText("Property Type");
                TextField numProps = new TextField();
                numProps.setPromptText("Number of Properties");
                form.getChildren().addAll(propType, numProps);
                form.setUserData(new PropertyTaxCalculator());
                break;
            case "Vehicles":
                TextField numVehicles = new TextField();
                numVehicles.setPromptText("Number of Vehicles");
                form.getChildren().add(numVehicles);
                form.setUserData(new VehicleTaxCalculator());
                break;
            case "GST":
                TextField numItems = new TextField();
                numItems.setPromptText("Number of Items Purchased");
                form.getChildren().add(numItems);
                form.setUserData(new GstTaxCalculator());
                break;
        }

        dynamicInputPane.getChildren().add(form);
    }

    @FXML
    private void calculateTax(ActionEvent event) {
        receiptItems.clear();

        for (Node child : dynamicInputPane.getChildren()) {
            if (child instanceof VBox form) {
                TaxCalculator calc = (TaxCalculator) form.getUserData();  // Polymorphism
                // Example: collect inputs and calculate (expand for real logic)
                double exampleAmount = 1000000;  // Replace with actual form data parsing
                double tax = calc.calculateTaxForCategory("", exampleAmount);
                receiptItems.add(new ReceiptItem("Item from form", exampleAmount, 1, tax));
            }
        }

        showReceipt();
    }

    private void showReceipt() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaxReceipt.fxml"));
            Parent root = loader.load();
            TaxReceiptController ctrl = loader.getController();
            double total = receiptItems.stream().mapToDouble(ReceiptItem::getTax).sum();
            ctrl.setReceiptData(receiptItems, total);
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 500));
            stage.setTitle("Tax Receipt");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("FBR Tax Portal - Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}