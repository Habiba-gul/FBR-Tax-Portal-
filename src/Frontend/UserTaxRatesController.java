package Frontend;

import Backend.TaxRange;
import Backend.TaxRangeService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class UserTaxRatesController {

    @FXML private ComboBox<String> categoryCombo;
    @FXML private ComboBox<String> subCategoryCombo;
    @FXML private HBox subCategoryBox;
    @FXML private TableView<TaxRange> rangesTable;
    @FXML private TableColumn<TaxRange, Double> minCol, maxCol, rateCol;

    private final TaxRangeService service = TaxRangeService.getInstance();

    @FXML
    public void initialize() {
        categoryCombo.getItems().addAll("Salary", "Property", "Vehicle", "GST");
        minCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));

        categoryCombo.valueProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                if (newVal.equals("Salary")) {
                    subCategoryCombo.getItems().setAll("Government", "Private", "Business");
                    subCategoryBox.setVisible(true);
                } else if (newVal.equals("Property")) {
                    subCategoryCombo.getItems().setAll("Residential", "Commercial");
                    subCategoryBox.setVisible(true);
                } else {
                    subCategoryBox.setVisible(false);
                    loadRanges();
                }
            }
        });

        subCategoryCombo.valueProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                loadRanges();
            }
        });
    }

    private void loadRanges() {
        String category = categoryCombo.getValue();
        String sub = subCategoryCombo.getValue();
        String dbCategory = "";
        if (category != null) {
            if (category.equals("Salary") && sub != null) {
                String subLower = sub.toLowerCase();
                if (subLower.equals("private")) {
                    subLower = "private_company";  // Fix for DB match
                } else if (subLower.equals("government")) {
                    subLower = "gov";
                } else if (subLower.equals("business")) {
                    subLower = "business";
                }
                dbCategory = "salary_" + subLower;
            } else if (category.equals("Property") && sub != null) {
                dbCategory = "property_" + sub.toLowerCase();
            } else if (category.equals("Vehicle")) {
                dbCategory = "vehicle";
            } else if (category.equals("GST")) {
                dbCategory = "gst";
            }

            System.out.println("Loading ranges for DB category: " + dbCategory); // Debug: Check console

            ObservableList<TaxRange> ranges = service.getRanges(dbCategory);
            System.out.println("Ranges found: " + ranges.size()); // Debug: Check count
            for (TaxRange r : ranges) {
                System.out.println("Range: min=" + r.getMinAmount() + ", max=" + r.getMaxAmount() + ", rate=" + r.getRate()); // Debug details
            }

            rangesTable.setItems(ranges);
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
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