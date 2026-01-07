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

public class GstTaxSettingsController {

    @FXML private TableView<TaxRange> gstTable;
    @FXML private TableColumn<TaxRange, Double> gstMinCol, gstMaxCol, gstRateCol;
    @FXML private TableColumn<TaxRange, Void> gstDeleteCol;

    private final TaxRangeService service = TaxRangeService.getInstance();

    @FXML
    public void initialize() {
        gstMinCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        gstMinCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        gstMinCol.setOnEditCommit(e -> updateRange(e.getRowValue(), e.getNewValue(), null, null));

        gstMaxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        gstMaxCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        gstMaxCol.setOnEditCommit(e -> updateRange(e.getRowValue(), null, e.getNewValue(), null));

        gstRateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        gstRateCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        gstRateCol.setOnEditCommit(e -> updateRange(e.getRowValue(), null, null, e.getNewValue()));

        gstDeleteCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    TaxRange range = getTableView().getItems().get(getIndex());
                    if (service.deleteRange(range)) {
                        gstTable.setItems(service.getRanges("gst"));
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

        gstTable.setItems(service.getRanges("gst"));
        gstTable.setEditable(true);
    }

    private void updateRange(TaxRange range, Double newMin, Double newMax, Double newRate) {
        if (newMin != null) range.setMinAmount(newMin);
        if (newMax != null) range.setMaxAmount(newMax);
        if (newRate != null) range.setRate(newRate);

        if (!service.updateRange(range)) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Could not update range.");
        }
    }

    @FXML
    private void addRange(ActionEvent event) {
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
                        if (service.addRange("gst", min, max, rate)) {
                            gstTable.setItems(service.getRanges("gst"));
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