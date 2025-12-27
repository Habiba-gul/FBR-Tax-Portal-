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

public class SalaryTaxSettingsController {

    @FXML private TableView<TaxRange> govTable;
    @FXML private TableColumn<TaxRange, Double> govMinCol, govMaxCol, govRateCol;
    @FXML private TableColumn<TaxRange, Void> govActionCol;

    @FXML private TableView<TaxRange> privateTable;
    @FXML private TableColumn<TaxRange, Double> privateMinCol, privateMaxCol, privateRateCol;
    @FXML private TableColumn<TaxRange, Void> privateActionCol;

    @FXML private TableView<TaxRange> businessTable;
    @FXML private TableColumn<TaxRange, Double> businessMinCol, businessMaxCol, businessRateCol;
    @FXML private TableColumn<TaxRange, Void> businessActionCol;

    private final TaxRangeService service = TaxRangeService.getInstance();

    @FXML
    public void initialize() {
        setupEditableTable(govTable, govMinCol, govMaxCol, govRateCol, govActionCol);
        setupEditableTable(privateTable, privateMinCol, privateMaxCol, privateRateCol, privateActionCol);
        setupEditableTable(businessTable, businessMinCol, businessMaxCol, businessRateCol, businessActionCol);

        refreshTables();
    }

    private void setupEditableTable(TableView<TaxRange> table, TableColumn<TaxRange, Double> minCol, TableColumn<TaxRange, Double> maxCol, TableColumn<TaxRange, Double> rateCol, TableColumn<TaxRange, Void> actionCol) {
        minCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));

        // Make columns editable
        minCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        maxCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        rateCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        // Commit edits to the model
        minCol.setOnEditCommit(e -> {
            e.getRowValue().setMinAmount(e.getNewValue());
            service.updateRange(e.getRowValue());  // Save immediately or on "Save All"
        });
        maxCol.setOnEditCommit(e -> {
            e.getRowValue().setMaxAmount(e.getNewValue());
            service.updateRange(e.getRowValue());
        });
        rateCol.setOnEditCommit(e -> {
            e.getRowValue().setRate(e.getNewValue());
            service.updateRange(e.getRowValue());
        });

        // Delete button
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(event -> {
                    TaxRange range = getTableView().getItems().get(getIndex());
                    service.deleteRange(range);
                    refreshTables();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    private void refreshTables() {
        govTable.setItems(service.getRanges("salary_gov"));
        privateTable.setItems(service.getRanges("salary_private"));
        businessTable.setItems(service.getRanges("salary_business"));

        enforceMinRanges("salary_gov", govTable);
        enforceMinRanges("salary_private", privateTable);
        enforceMinRanges("salary_business", businessTable);
    }

    private void enforceMinRanges(String category, TableView<TaxRange> table) {
        if (table.getItems().size() < 2) {
            service.addRange(category, 0, 500000, 5.0);
            service.addRange(category, 500001, Double.MAX_VALUE, 15.0);
            refreshTables();
        }
    }

    @FXML private void addGovRange(ActionEvent event) { addRange("salary_gov"); }
    @FXML private void addPrivateRange(ActionEvent event) { addRange("salary_private"); }
    @FXML private void addBusinessRange(ActionEvent event) { addRange("salary_business"); }

    private void addRange(String category) {
        service.addRange(category, 0, 0, 0);
        refreshTables();
    }

    @FXML
    private void saveAll(ActionEvent event) {
        // Optional: extra save if needed, but edits are already committed
        new Alert(Alert.AlertType.INFORMATION, "All changes saved to database!").show();
        refreshTables();
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