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

public class VehicleTaxSettingsController {

    @FXML private TableView<TaxRange> vehicleTable;
    @FXML private TableColumn<TaxRange, Double> vehMinCol, vehMaxCol, vehRateCol;
    @FXML private TableColumn<TaxRange, Void> vehActionCol;

    private final TaxRangeService service = TaxRangeService.getInstance();

    @FXML
    public void initialize() {
        setupEditableTable(vehicleTable, vehMinCol, vehMaxCol, vehRateCol, vehActionCol);

        refreshTable();
    }

    private void setupEditableTable(TableView<TaxRange> table, TableColumn<TaxRange, Double> minCol, TableColumn<TaxRange, Double> maxCol, TableColumn<TaxRange, Double> rateCol, TableColumn<TaxRange, Void> actionCol) {
        minCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));

        minCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        maxCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        rateCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        minCol.setOnEditCommit(e -> {
            e.getRowValue().setMinAmount(e.getNewValue());
            service.updateRange(e.getRowValue());
        });
        maxCol.setOnEditCommit(e -> {
            e.getRowValue().setMaxAmount(e.getNewValue());
            service.updateRange(e.getRowValue());
        });
        rateCol.setOnEditCommit(e -> {
            e.getRowValue().setRate(e.getNewValue());
            service.updateRange(e.getRowValue());
        });

        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(event -> {
                    TaxRange range = getTableView().getItems().get(getIndex());
                    service.deleteRange(range);
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    private void refreshTable() {
        vehicleTable.setItems(service.getRanges("vehicle"));
        enforceMinRanges();
    }

    private void enforceMinRanges() {
        if (vehicleTable.getItems().size() < 2) {
            service.addRange("vehicle", 0, 5000000, 2.0);
            service.addRange("vehicle", 5000001, Double.MAX_VALUE, 5.0);
            refreshTable();
        }
    }

    @FXML private void addRange(ActionEvent event) {
        service.addRange("vehicle", 0, 0, 0);
        refreshTable();
    }

    @FXML
    private void saveAll(ActionEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "All changes saved!").show();
        refreshTable();
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
}