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
        refreshTables();
    }

    private void setupTable(TableView<TaxRange> table, TableColumn<TaxRange, Double> minCol,
                            TableColumn<TaxRange, Double> maxCol, TableColumn<TaxRange, Double> rateCol,
                            TableColumn<TaxRange, Void> actionCol, String category) {
        minCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));

        minCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        maxCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        rateCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        minCol.setOnEditCommit(e -> {
            TaxRange r = e.getRowValue();
            double oldVal = r.getMinAmount();
            r.setMinAmount(e.getNewValue());
            if (r.getMinAmount() > r.getMaxAmount()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Min cannot be greater than Max. Reverting.");
                r.setMinAmount(oldVal);
                table.refresh();
            }
        });

        maxCol.setOnEditCommit(e -> {
            TaxRange r = e.getRowValue();
            double oldVal = r.getMaxAmount();
            r.setMaxAmount(e.getNewValue());
            if (r.getMinAmount() > r.getMaxAmount()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Max cannot be less than Min. Reverting.");
                r.setMaxAmount(oldVal);
                table.refresh();
            }
        });

        rateCol.setOnEditCommit(e -> {
            TaxRange r = e.getRowValue();
            r.setRate(e.getNewValue());
            // No DB update here - deferred to saveAll
        });

        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");
            {
                deleteBtn.setOnAction(evt -> {
                    TaxRange range = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this range?");
                    if (confirm.showAndWait().get() == ButtonType.OK) {
                        boolean success = service.deleteRange(range);
                        if (success) {
                            getTableView().getItems().remove(range);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete range from database.");
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        table.setItems(service.getRanges(category));
    }

    private void refreshTables() {
        residentialTable.setItems(service.getRanges("property_residential"));
        commercialTable.setItems(service.getRanges("property_commercial"));
        residentialTable.refresh();
        commercialTable.refresh();
    }

    @FXML
    private void addResidentialRange(ActionEvent event) { addRange("property_residential"); }

    @FXML
    private void addCommercialRange(ActionEvent event) { addRange("property_commercial"); }

    private void addRange(String category) {
        boolean success = service.addRange(category, 0, 0, 0);
        if (success) {
            refreshTables();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add new range to database.");
        }
    }

    @FXML
private void saveAll(ActionEvent event) {
    boolean allSuccess = true;

    // Residential
    for (TaxRange r : residentialTable.getItems()) {
        if (!service.updateRange(r)) {
            allSuccess = false;
            System.out.println("Failed to update Property Residential range ID: " + r.getId());
        }
    }

    // Commercial
    for (TaxRange r : commercialTable.getItems()) {
        if (!service.updateRange(r)) {
            allSuccess = false;
            System.out.println("Failed to update Property Commercial range ID: " + r.getId());
        }
    }

    if (allSuccess) {
        showAlert(Alert.AlertType.INFORMATION, "Success", "All property tax ranges saved successfully!");
        refreshTables();
    } else {
        showAlert(Alert.AlertType.ERROR, "Partial Failure", "Some property tax ranges failed to save. Check console.");
    }
}

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaxRateSettings.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
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