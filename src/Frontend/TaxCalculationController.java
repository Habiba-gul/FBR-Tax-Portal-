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
        TextField salaryField = new TextField();
        salaryField.setPromptText("Annual Salary (PKR)");
        Button removeBtn = new Button("Remove");

        form.getChildren().addAll(title, jobType, salaryField, removeBtn);
        dynamicInputPane.getChildren().add(form);

        removeBtn.setOnAction(e -> dynamicInputPane.getChildren().remove(form));
    }

    @FXML
    private void addPropertyForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("Property Details");
        ComboBox<String> type = new ComboBox<>(FXCollections.observableArrayList("Residential", "Commercial"));
        TextField priceField = new TextField();
        priceField.setPromptText("Property Value (PKR)");
        Button removeBtn = new Button("Remove");

        form.getChildren().addAll(title, type, priceField, removeBtn);
        dynamicInputPane.getChildren().add(form);

        removeBtn.setOnAction(e -> dynamicInputPane.getChildren().remove(form));
    }

    @FXML
    private void addVehicleForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("Vehicle Details");
        TextField priceField = new TextField();
        priceField.setPromptText("Vehicle Value (PKR)");
        Button removeBtn = new Button("Remove");

        form.getChildren().addAll(title, priceField, removeBtn);
        dynamicInputPane.getChildren().add(form);

        removeBtn.setOnAction(e -> dynamicInputPane.getChildren().remove(form));
    }

    @FXML
    private void addGstForm(ActionEvent event) {
        VBox form = new VBox(10);
        Label title = new Label("GST Details");
        TextField amountField = new TextField();
        amountField.setPromptText("Purchase Amount (PKR)");
        Button removeBtn = new Button("Remove");

        form.getChildren().addAll(title, amountField, removeBtn);
        dynamicInputPane.getChildren().add(form);

        removeBtn.setOnAction(e -> dynamicInputPane.getChildren().remove(form));
    }

    @FXML
    private void calculateTax(ActionEvent event) {
        double totalTax = 0.0;
        receiptItems.clear();

        for (Node node : dynamicInputPane.getChildren()) {
            if (node instanceof VBox form) {
                Label title = (Label) form.getChildren().get(0);
                String formType = title.getText().replace(" Details", "");

                if (formType.equals("Salary")) {
                    ComboBox<String> jobType = (ComboBox<String>) form.getChildren().get(1);
                    TextField salaryField = (TextField) form.getChildren().get(2);
                    if (jobType.getValue() != null && !salaryField.getText().isEmpty()) {
                        double salary = Double.parseDouble(salaryField.getText());
                        double tax = new SalaryTaxCalculator().calculateTaxForCategory(jobType.getValue(), salary);
                        totalTax += tax;
                        receiptItems.add(new ReceiptItem("Salary (" + jobType.getValue() + ")", salary, 1, tax));
                    }
                } else if (formType.equals("Property")) {
                    ComboBox<String> type = (ComboBox<String>) form.getChildren().get(1);
                    TextField priceField = (TextField) form.getChildren().get(2);
                    if (type.getValue() != null && !priceField.getText().isEmpty()) {
                        double price = Double.parseDouble(priceField.getText());
                        double tax = new PropertyTaxCalculator().calculateTaxForCategory(type.getValue(), price);
                        System.out.println("Property tax calculated: " + tax + " for value " + price + " (" + type.getValue() + ")"); // Debug
                        totalTax += tax;
                        receiptItems.add(new ReceiptItem("Property (" + type.getValue() + ")", price, 1, tax));
                    }
                } else if (formType.equals("Vehicle")) {
                    TextField priceField = (TextField) form.getChildren().get(1);
                    if (!priceField.getText().isEmpty()) {
                        double price = Double.parseDouble(priceField.getText());
                        double tax = new VehicleTaxCalculator().calculateTaxForCategory("", price);
                        totalTax += tax;
                        receiptItems.add(new ReceiptItem("Vehicle", price, 1, tax));
                    }
                } else if (formType.equals("GST")) {
                    TextField amountField = (TextField) form.getChildren().get(1);
                    if (!amountField.getText().isEmpty()) {
                        double amount = Double.parseDouble(amountField.getText());
                        double tax = new GstTaxCalculator().calculateTaxForCategory("", amount);
                        totalTax += tax;
                        receiptItems.add(new ReceiptItem("GST", amount, 1, tax));
                    }
                }
            }
        }

        if (receiptItems.isEmpty()) {
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