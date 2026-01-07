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
    @FXML private TableColumn<TaxRange, Void> govDeleteCol;

    @FXML private TableView<TaxRange> privateTable;
    @FXML private TableColumn<TaxRange, Double> privMinCol, privMaxCol, privRateCol;
    @FXML private TableColumn<TaxRange, Void> privDeleteCol;

    @FXML private TableView<TaxRange> businessTable;
    @FXML private TableColumn<TaxRange, Double> busMinCol, busMaxCol, busRateCol;
    @FXML private TableColumn<TaxRange, Void> busDeleteCol;

    private final TaxRangeService service = TaxRangeService.getInstance();

    @FXML
    public void initialize() {
        setupTable(govTable, govMinCol, govMaxCol, govRateCol, govDeleteCol, "salary_gov");
        setupTable(privateTable, privMinCol, privMaxCol, privRateCol, privDeleteCol, "salary_private_company");
        setupTable(businessTable, busMinCol, busMaxCol, busRateCol, busDeleteCol, "salary_business");
    }

    private void setupTable(TableView<TaxRange> table,
                            TableColumn<TaxRange, Double> minCol,
                            TableColumn<TaxRange, Double> maxCol,
                            TableColumn<TaxRange, Double> rateCol,
                            TableColumn<TaxRange, Void> deleteCol,
                            String category) {

        minCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        minCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        minCol.setOnEditCommit(e -> updateRange(e.getRowValue(), e.getNewValue(), null, null));

        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        maxCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        maxCol.setOnEditCommit(e -> updateRange(e.getRowValue(), null, e.getNewValue(), null));

        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        rateCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        rateCol.setOnEditCommit(e -> updateRange(e.getRowValue(), null, null, e.getNewValue()));

        deleteCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    TaxRange range = getTableView().getItems().get(getIndex());
                    if (service.deleteRange(range)) {
                        table.setItems(service.getRanges(category));
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Delete Failed", "Could not delete range.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        table.setItems(service.getRanges(category));
        table.setEditable(true);
    }

    private void updateRange(TaxRange range, Double newMin, Double newMax, Double newRate) {
        if (newMin != null) range.setMinAmount(newMin);
        if (newMax != null) range.setMaxAmount(newMax);
        if (newRate != null) range.setRate(newRate);

        if (!service.updateRange(range)) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Could not update range.");
        }
    }

    @FXML private void addGovRange(ActionEvent event) { addRange("salary_gov", govTable); }
    @FXML private void addPrivateRange(ActionEvent event) { addRange("salary_private_company", privateTable); }
    @FXML private void addBusinessRange(ActionEvent event) { addRange("salary_business", businessTable); }

    private void addRange(String category, TableView<TaxRange> table) {
        TextInputDialog minDialog = new TextInputDialog("0");
        minDialog.setTitle("Add New Range");
        minDialog.setHeaderText("Min Amount");
        minDialog.showAndWait().ifPresent(minStr -> {
            try {
                double min = Double.parseDouble(minStr);
                TextInputDialog maxDialog = new TextInputDialog("Infinity");
                maxDialog.setHeaderText("Max Amount");
                maxDialog.showAndWait().ifPresent(maxStr -> {
                    double max = maxStr.equalsIgnoreCase("Infinity") ? Double.MAX_VALUE : Double.parseDouble(maxStr);
                    TextInputDialog rateDialog = new TextInputDialog("0");
                    rateDialog.setHeaderText("Rate (%)");
                    rateDialog.showAndWait().ifPresent(rateStr -> {
                        double rate = Double.parseDouble(rateStr);
                        if (service.addRange(category, min, max, rate)) {
                            table.setItems(service.getRanges(category));
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Add Failed", "Could not add range.");
                        }
                    });
                });
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers.");
            }
        });
    }

    @FXML
    private void saveAll(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Saved", "All changes have been saved to the database.");
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