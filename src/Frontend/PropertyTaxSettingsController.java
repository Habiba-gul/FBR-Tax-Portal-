package Frontend;

import Backend.TaxRange;
import Backend.TaxRangeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;

public class PropertyTaxSettingsController {

    @FXML private TableView<TaxRange> residentialTable;
    @FXML private TableColumn<TaxRange, Double> resMinCol, resMaxCol, resRateCol;
    @FXML private TableColumn<TaxRange, Void> resActionCol;

    @FXML private TableView<TaxRange> commercialTable;
    @FXML private TableColumn<TaxRange, Double> commMinCol, commMaxCol, commRateCol;
    @FXML private TableColumn<TaxRange, Void> commActionCol;

    private final TaxRangeService service = TaxRangeService.getInstance();

    @FXML
    public void initialize() {
        setupTable(residentialTable, resMinCol, resMaxCol, resRateCol, resActionCol, "property_residential");
        setupTable(commercialTable, commMinCol, commMaxCol, commRateCol, commActionCol, "property_commercial");
    }

    private void setupTable(TableView<TaxRange> table, TableColumn<TaxRange, Double> minCol, TableColumn<TaxRange, Double> maxCol, 
                            TableColumn<TaxRange, Double> rateCol, TableColumn<TaxRange, Void> actionCol, String category) {
        minCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        minCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        minCol.setOnEditCommit(e -> e.getRowValue().setMinAmount(e.getNewValue()));

        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        maxCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        maxCol.setOnEditCommit(e -> e.getRowValue().setMaxAmount(e.getNewValue()));

        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        rateCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        rateCol.setOnEditCommit(e -> e.getRowValue().setRate(e.getNewValue()));

        actionCol.setCellFactory(col -> new TableCell<TaxRange, Void>() {
            private final Button deleteBtn = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    deleteBtn.setOnAction(event -> {
                        TaxRange range = getTableView().getItems().get(getIndex());
                        if (service.deleteRange(range)) {
                            table.getItems().remove(range);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete range.");
                        }
                    });
                    setGraphic(deleteBtn);
                }
            }
        });

        table.setItems(service.getRanges(category));
    }

    @FXML
    private void addResidentialRange(ActionEvent event) {
        addRangeToTable(residentialTable, "property_residential");
    }

    @FXML
    private void addCommercialRange(ActionEvent event) {
        addRangeToTable(commercialTable, "property_commercial");
    }

    private void addRangeToTable(TableView<TaxRange> table, String category) {
        if (service.addRange(category, 0, 0, 0)) {
            table.setItems(service.getRanges(category)); // Reload to get new ID
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add new range.");
        }
    }

    @FXML
    private void saveAll(ActionEvent event) {
        int changes = 0;
        changes += saveTable(residentialTable);
        changes += saveTable(commercialTable);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Saved " + changes + " changes to database.");
    }

    private int saveTable(TableView<TaxRange> table) {
        int count = 0;
        for (TaxRange range : table.getItems()) {
            if (service.updateRange(range)) {
                count++;
                System.out.println("Updated range ID " + range.getId() + " in DB (Property)");
            } else {
                System.out.println("Failed to update range ID " + range.getId() + " (Property)");
            }
        }
        return count;
    }

    @FXML
    private void handleBack(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TaxRateSettings.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("FBR Tax Portal - Tax Rate Settings");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}