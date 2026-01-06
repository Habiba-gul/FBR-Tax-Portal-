package Frontend;

import Backend.TaxRange;
import Backend.TaxRangeService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;  // This import was missing!
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
        categoryCombo.getItems().addAll("Salary", "Property", "Vehicles", "GST");

        minCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));

        categoryCombo.valueProperty().addListener((obs, old, val) -> {
            subCategoryBox.setVisible(!"Vehicles".equals(val) && !"GST".equals(val));
            subCategoryCombo.getItems().clear();
            if ("Salary".equals(val)) {
                subCategoryCombo.getItems().addAll("Government", "Private", "Business");
            } else if ("Property".equals(val)) {
                subCategoryCombo.getItems().addAll("Residential", "Commercial");
            }
            rangesTable.getItems().clear();
        });

        subCategoryCombo.valueProperty().addListener((obs, old, val) -> rangesTable.getItems().clear());
    }

    @FXML
    private void handleShowRanges(ActionEvent event) {
        String category = categoryCombo.getValue();
        String sub = subCategoryCombo.getValue();

        if (category == null) return;

        String dbCategory;
        if ("Vehicles".equals(category) || "GST".equals(category)) {
            dbCategory = category.toLowerCase();
        } else {
            if ("Salary".equals(category)) {
                switch (sub.toLowerCase()) {
                    case "government":
                        dbCategory = "salary_gov";
                        break;
                    case "private":
                        dbCategory = "salary_private_company";
                        break;
                    case "business":
                        dbCategory = "salary_business";
                        break;
                    default:
                        dbCategory = "";
                }
            } else {
                dbCategory = category.toLowerCase() + "_" + sub.toLowerCase();
            }
        }

        ObservableList<TaxRange> ranges = service.getRanges(dbCategory);
        rangesTable.setItems(ranges);
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("FBR Tax Portal - Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}