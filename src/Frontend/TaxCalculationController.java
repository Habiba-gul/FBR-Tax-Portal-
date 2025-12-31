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
        form.getChildren().addAll(title, jobType, salaryField);
        form.setUserData(new SalaryTaxCalculator());
        dynamicInputPane.getChildren().add(form);
    }

    @FXML
    private void addPropertyForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("Property Details");
        ComboBox<String> propType = new ComboBox<>(FXCollections.observableArrayList("Residential", "Commercial"));
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
        // Clear previous price fields
        form.getChildren().removeIf(node -> node instanceof TextField && ((TextField) node).getPromptText().contains("Price for"));
        try {
            int num = Integer.parseInt(numStr);
            for (int i = 1; i <= num; i++) {
                TextField priceField = new TextField();
                priceField.setPromptText("Price for " + type + " Property " + i + " (PKR)");
                form.getChildren().add(priceField);
            }
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Enter a valid number").show();
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
        form.getChildren().removeIf(node -> node instanceof TextField && ((TextField) node).getPromptText().contains("Price for Vehicle"));
        try {
            int num = Integer.parseInt(numStr);
            for (int i = 1; i <= num; i++) {
                TextField priceField = new TextField();
                priceField.setPromptText("Price for Vehicle " + i + " (PKR)");
                form.getChildren().add(priceField);
            }
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Enter a valid number").show();
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
        form.getChildren().removeIf(node -> node instanceof HBox);
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
            new Alert(Alert.AlertType.ERROR, "Enter a valid number").show();
        }
    }

    @FXML
    private void calculateTax(ActionEvent event) {
        receiptItems.clear();
        double totalTax = 0.0;
        boolean hasValidData = false;

        for (Node child : dynamicInputPane.getChildren()) {
            if (child instanceof VBox form) {
                TaxCalculator calc = (TaxCalculator) form.getUserData();
                if (calc == null) continue;

                for (Node node : form.getChildren()) {
                    if (node instanceof TextField tf) {
                        String text = tf.getText().trim();
                        if (!text.isEmpty()) {
                            try {
                                double amount = Double.parseDouble(text);
                                double tax = calc.calculateTaxForCategory("", amount);
                                receiptItems.add(new ReceiptItem(tf.getPromptText(), amount, 1, tax));
                                totalTax += tax;
                                hasValidData = true;
                            } catch (NumberFormatException ignored) {}
                        }
                    } else if (node instanceof HBox itemBox) {
                        // GST items
                        TextField descField = (TextField) itemBox.getChildren().get(0);
                        TextField priceField = (TextField) itemBox.getChildren().get(1);
                        TextField qtyField = (TextField) itemBox.getChildren().get(2);

                        String desc = descField.getText().trim();
                        String priceText = priceField.getText().trim();
                        String qtyText = qtyField.getText().trim();

                        if (!priceText.isEmpty() && !qtyText.isEmpty()) {
                            try {
                                double price = Double.parseDouble(priceText);
                                int qty = Integer.parseInt(qtyText);
                                double totalAmount = price * qty;
                                double tax = calc.calculateTaxForCategory("", totalAmount);
                                receiptItems.add(new ReceiptItem(desc.isEmpty() ? "GST Item" : desc, price, qty, tax));
                                totalTax += tax;
                                hasValidData = true;
                            } catch (NumberFormatException ignored) {}
                        }
                    }
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