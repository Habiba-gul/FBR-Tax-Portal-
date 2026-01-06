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
    private void addSalaryForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("Salary Details");
        ComboBox<String> jobType = new ComboBox<>(FXCollections.observableArrayList("Government", "Private", "Business"));
        jobType.setPromptText("Job Type");
        TextField salaryField = new TextField();
        salaryField.setPromptText("Annual Salary (PKR)");
        Button removeBtn = new Button("Remove");
        removeBtn.setOnAction(e -> dynamicInputPane.getChildren().remove(form));

        form.getChildren().addAll(title, jobType, salaryField, removeBtn);
        dynamicInputPane.getChildren().add(form);
    }

    @FXML
    private void addPropertyForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("Property Details");
        ComboBox<String> propType = new ComboBox<>(FXCollections.observableArrayList("Residential", "Commercial"));
        propType.setPromptText("Property Type");
        TextField valueField = new TextField();
        valueField.setPromptText("Property Value (PKR)");
        Button removeBtn = new Button("Remove");
        removeBtn.setOnAction(e -> dynamicInputPane.getChildren().remove(form));

        form.getChildren().addAll(title, propType, valueField, removeBtn);
        dynamicInputPane.getChildren().add(form);
    }

    @FXML
    private void addVehicleForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("Vehicle Details");
        TextField priceField = new TextField();
        priceField.setPromptText("Vehicle Price (PKR)");
        Button removeBtn = new Button("Remove");
        removeBtn.setOnAction(e -> dynamicInputPane.getChildren().remove(form));

        form.getChildren().addAll(title, priceField, removeBtn);
        dynamicInputPane.getChildren().add(form);
    }

    @FXML
    private void addGstForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("GST Details");
        TextField amountField = new TextField();
        amountField.setPromptText("Transaction Amount (PKR)");
        Button removeBtn = new Button("Remove");
        removeBtn.setOnAction(e -> dynamicInputPane.getChildren().remove(form));

        form.getChildren().addAll(title, amountField, removeBtn);
        dynamicInputPane.getChildren().add(form);
    }

    @FXML
    private void calculateTax(ActionEvent event) {
        receiptItems.clear();
        double totalTax = 0.0;
        boolean hasValidData = false;

        for (Node node : dynamicInputPane.getChildren()) {
            if (node instanceof VBox form) {
                Label title = (Label) form.getChildren().get(0);
                String type = title.getText();

                if (type.contains("Salary")) {
                    ComboBox<String> jobType = (ComboBox<String>) form.getChildren().get(1);
                    TextField salaryField = (TextField) form.getChildren().get(2);
                    try {
                        double salary = Double.parseDouble(salaryField.getText());
                        if (salary > 0 && jobType.getValue() != null) {
                            SalaryTaxCalculator calc = new SalaryTaxCalculator();
                            double tax = calc.calculateTaxForCategory(jobType.getValue(), salary);
                            receiptItems.add(new ReceiptItem("Salary (" + jobType.getValue() + ")", salary, 1, tax));
                            totalTax += tax;
                            hasValidData = true;
                        }
                    } catch (NumberFormatException ignored) {}
                } else if (type.contains("Property")) {
                    ComboBox<String> propType = (ComboBox<String>) form.getChildren().get(1);
                    TextField valueField = (TextField) form.getChildren().get(2);
                    try {
                        double value = Double.parseDouble(valueField.getText());
                        if (value > 0 && propType.getValue() != null) {
                            PropertyTaxCalculator calc = new PropertyTaxCalculator();
                            double tax = calc.calculateTaxForCategory(propType.getValue(), value);
                            receiptItems.add(new ReceiptItem("Property (" + propType.getValue() + ")", value, 1, tax));
                            totalTax += tax;
                            hasValidData = true;
                        }
                    } catch (NumberFormatException ignored) {}
                } else if (type.contains("Vehicle")) {
                    TextField priceField = (TextField) form.getChildren().get(1);
                    try {
                        double price = Double.parseDouble(priceField.getText());
                        if (price > 0) {
                            VehicleTaxCalculator calc = new VehicleTaxCalculator();
                            double tax = calc.calculateTaxForCategory("", price);
                            receiptItems.add(new ReceiptItem("Vehicle", price, 1, tax));
                            totalTax += tax;
                            hasValidData = true;
                        }
                    } catch (NumberFormatException ignored) {}
                } else if (type.contains("GST")) {
                    TextField amountField = (TextField) form.getChildren().get(1);
                    try {
                        double amount = Double.parseDouble(amountField.getText());
                        if (amount > 0) {
                            GstTaxCalculator calc = new GstTaxCalculator();
                            double tax = calc.calculateTaxForCategory("", amount);
                            receiptItems.add(new ReceiptItem("GST Transaction", amount, 1, tax));
                            totalTax += tax;
                            hasValidData = true;
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        if (!hasValidData) {
            new Alert(Alert.AlertType.WARNING, "No valid data entered.").show();
            return;
        }

        SystemManager.setTotalTax(totalTax);
        SystemManager.setReceiptItems(receiptItems);

        new Alert(Alert.AlertType.INFORMATION, "Tax calculated successfully! Go to Payment and Transaction for details.").show();
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