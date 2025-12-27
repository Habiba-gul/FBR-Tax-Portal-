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
        categoryCombo.setOnAction(e -> updateSubCategories());

        minCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getMinAmount()).asObject());
        maxCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getMaxAmount()).asObject());
        rateCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getRate()).asObject());
    }

    private void updateSubCategories() {
        String cat = categoryCombo.getValue();
        subCategoryCombo.getItems().clear();
        subCategoryBox.setVisible(true);

        if ("Salary".equals(cat)) {
            subCategoryCombo.getItems().addAll("Government", "Private", "Business");
        } else if ("Property".equals(cat)) {
            subCategoryCombo.getItems().addAll("Residential", "Commercial");
        } else {
            subCategoryBox.setVisible(false);
        }
    }

    @FXML
    private void handleShowRanges(ActionEvent event) {
        String category = categoryCombo.getValue();
        String sub = subCategoryCombo.getValue();

        String dbCategory;
        if ("Vehicles".equals(category) || "GST".equals(category)) {
            dbCategory = category.toLowerCase();
        } else {
            dbCategory = category.toLowerCase() + "_" + sub.toLowerCase();
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