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
    @FXML private TableColumn<TaxRange, Void> gstActionCol;

    private final TaxRangeService service = TaxRangeService.getInstance();

    @FXML
    public void initialize() {
        gstMinCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        gstMaxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        gstRateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));

        gstMinCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        gstMaxCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        gstRateCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        gstMinCol.setOnEditCommit(e -> {
            TaxRange r = e.getRowValue();
            double oldVal = r.getMinAmount();
            r.setMinAmount(e.getNewValue());
            if (r.getMinAmount() > r.getMaxAmount()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Min cannot be greater than Max. Reverting.");
                r.setMinAmount(oldVal);
                gstTable.refresh();
            }
        });

        gstMaxCol.setOnEditCommit(e -> {
            TaxRange r = e.getRowValue();
            double oldVal = r.getMaxAmount();
            r.setMaxAmount(e.getNewValue());
            if (r.getMinAmount() > r.getMaxAmount()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Max cannot be less than Min. Reverting.");
                r.setMaxAmount(oldVal);
                gstTable.refresh();
            }
        });

        gstRateCol.setOnEditCommit(e -> {
            TaxRange r = e.getRowValue();
            r.setRate(e.getNewValue());
            // No DB update here - deferred to saveAll
        });

        gstActionCol.setCellFactory(col -> new TableCell<>() {
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

        refreshTable();
    }

    private void refreshTable() {
        gstTable.setItems(service.getRanges("gst"));
        gstTable.refresh();
    }

    @FXML private void addRange(ActionEvent event) {
        boolean success = service.addRange("gst", 0, 0, 0);
        if (success) {
            refreshTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add new range to database.");
        }
    }

    @FXML
private void saveAll(ActionEvent event) {
    boolean allSuccess = true;

    for (TaxRange r : gstTable.getItems()) {
        if (!service.updateRange(r)) {
            allSuccess = false;
            System.out.println("Failed to update GST range ID: " + r.getId());
        }
    }

    if (allSuccess) {
        showAlert(Alert.AlertType.INFORMATION, "Success", "All GST tax ranges saved successfully!");
        refreshTable();
    } else {
        showAlert(Alert.AlertType.ERROR, "Partial Failure", "Some GST tax ranges failed to save. Check console.");
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