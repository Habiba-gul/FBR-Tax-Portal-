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
        setupTable(govTable, govMinCol, govMaxCol, govRateCol, govActionCol, "salary_gov");
        setupTable(privateTable, privateMinCol, privateMaxCol, privateRateCol, privateActionCol, "salary_private_company");
        setupTable(businessTable, businessMinCol, businessMaxCol, businessRateCol, businessActionCol, "salary_business");
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
        govTable.setItems(service.getRanges("salary_gov"));
        privateTable.setItems(service.getRanges("salary_private_company"));
        businessTable.setItems(service.getRanges("salary_business"));
        govTable.refresh();
        privateTable.refresh();
        businessTable.refresh();
    }

    @FXML
    private void addGovRange(ActionEvent event) { addRange("salary_gov"); }

    @FXML
    private void addPrivateRange(ActionEvent event) { addRange("salary_private_company"); }

    @FXML
    private void addBusinessRange(ActionEvent event) { addRange("salary_business"); }

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

    // Government
    for (TaxRange r : govTable.getItems()) {
        if (!service.updateRange(r)) {
            allSuccess = false;
            System.out.println("Failed to update Salary Government range ID: " + r.getId());
        }
    }

    // Private
    for (TaxRange r : privateTable.getItems()) {
        if (!service.updateRange(r)) {
            allSuccess = false;
            System.out.println("Failed to update Salary Private range ID: " + r.getId());
        }
    }

    // Business
    for (TaxRange r : businessTable.getItems()) {
        if (!service.updateRange(r)) {
            allSuccess = false;
            System.out.println("Failed to update Salary Business range ID: " + r.getId());
        }
    }

    if (allSuccess) {
        showAlert(Alert.AlertType.INFORMATION, "Success", "All salary tax ranges saved successfully!");
        refreshTables();
    } else {
        showAlert(Alert.AlertType.ERROR, "Partial Failure", "Some salary tax ranges failed to save. Check console for details.");
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