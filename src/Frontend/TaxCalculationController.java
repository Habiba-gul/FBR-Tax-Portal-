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
import javafx.scene.layout.HBox;
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
        // No init needed
    }

    @FXML
    private void addSalaryForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("Salary Details");
        ComboBox<String> jobType = new ComboBox<>(FXCollections.observableArrayList("Government", "Private", "Business"));
        jobType.setPromptText("Job Type");
        TextField salaryField = new TextField();
        salaryField.setPromptText("Annual Salary (PKR)");
        form.getChildren().addAll(title, jobType, salaryField);
        form.setUserData(new SalaryTaxCalculator());
        dynamicInputPane.getChildren().add(form);
    }

    @FXML
    private void addPropertyForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("Property Details");
        ComboBox<String> propType = new ComboBox<>(FXCollections.observableArrayList("Residential", "Commercial", "Both"));
        propType.setPromptText("Property Type");
        TextField numProps = new TextField();
        numProps.setPromptText("Number of Properties");
        Button generateBtn = new Button("Generate Price Fields");
        generateBtn.setOnAction(e -> generatePropertyFields(form, propType.getValue(), numProps.getText()));
        form.getChildren().addAll(title, propType, numProps, generateBtn);
        form.setUserData(new PropertyTaxCalculator());
        dynamicInputPane.getChildren().add(form);
    }

    private void generatePropertyFields(VBox form, String type, String numStr) {
        try {
            int num = Integer.parseInt(numStr);
            for (int i = 1; i <= num; i++) {
                TextField priceField = new TextField();
                priceField.setPromptText("Price for " + type + " Property " + i + " (PKR)");
                form.getChildren().add(priceField);
            }
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid number for properties").show();
        }
    }

    @FXML
    private void addVehiclesForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("Vehicles Details");
        TextField numVehicles = new TextField();
        numVehicles.setPromptText("Number of Vehicles");
        Button generateBtn = new Button("Generate Price Fields");
        generateBtn.setOnAction(e -> generateVehicleFields(form, numVehicles.getText()));
        form.getChildren().addAll(title, numVehicles, generateBtn);
        form.setUserData(new VehicleTaxCalculator());
        dynamicInputPane.getChildren().add(form);
    }

    private void generateVehicleFields(VBox form, String numStr) {
        try {
            int num = Integer.parseInt(numStr);
            for (int i = 1; i <= num; i++) {
                TextField priceField = new TextField();
                priceField.setPromptText("Price for Vehicle " + i + " (PKR)");
                form.getChildren().add(priceField);
            }
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid number for vehicles").show();
        }
    }

    @FXML
    private void addGstForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("GST Details");
        TextField numItems = new TextField();
        numItems.setPromptText("Number of Items");
        Button generateBtn = new Button("Generate Item Fields");
        generateBtn.setOnAction(e -> generateGstFields(form, numItems.getText()));
        form.getChildren().addAll(title, numItems, generateBtn);
        form.setUserData(new GstTaxCalculator());
        dynamicInputPane.getChildren().add(form);
    }

    private void generateGstFields(VBox form, String numStr) {
        try {
            int num = Integer.parseInt(numStr);
            for (int i = 1; i <= num; i++) {
                HBox itemBox = new HBox(10);
                TextField descField = new TextField();
                descField.setPromptText("Description " + i);
                TextField priceField = new TextField();
                priceField.setPromptText("Price " + i + " (PKR)");
                TextField qtyField = new TextField();
                qtyField.setPromptText("Quantity " + i);
                itemBox.getChildren().addAll(descField, priceField, qtyField);
                form.getChildren().add(itemBox);
            }
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid number for items").show();
        }
    }

    @FXML
    private void calculateTax(ActionEvent event) {
        receiptItems.clear();

        for (Node child : dynamicInputPane.getChildren()) {
            if (child instanceof VBox form) {
                TaxCalculator calc = (TaxCalculator) form.getUserData();
                if (calc == null) continue;

                // Collect all entered prices (simplified - expand for real logic)
                List<Double> prices = new ArrayList<>();
                for (Node node : form.getChildren()) {
                    if (node instanceof TextField tf && tf.getPromptText().contains("Price")) {
                        try {
                            double price = Double.parseDouble(tf.getText());
                            prices.add(price);
                        } catch (NumberFormatException ignored) {}
                    }
                }

                if (!prices.isEmpty()) {
                    double totalTax = calc.calculateTaxForCategory("", prices.stream().mapToDouble(Double::doubleValue).toArray());
                    receiptItems.add(new ReceiptItem("Items from form", prices.get(0), prices.size(), totalTax));
                }
            }
        }

        if (receiptItems.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No valid data entered").show();
            return;
        }

        // Show success message only - no navigation
    Alert success = new Alert(Alert.AlertType.INFORMATION);
    success.setTitle("Tax Calculated");
    success.setHeaderText("Success!");
    success.setContentText("Tax calculated successfully!\nFor details, go to Payment and Transaction.");
    success.showAndWait();

        // Navigate to PaymentTransaction.fxml (add this if you have it)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentTransaction.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Payment & Transaction Details");
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