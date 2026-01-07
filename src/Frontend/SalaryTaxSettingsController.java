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
    @FXML private TableColumn<TaxRange, Double> privMinCol, privMaxCol, privRateCol;
    @FXML private TableColumn<TaxRange, Void> privActionCol;

    @FXML private TableView<TaxRange> businessTable;
    @FXML private TableColumn<TaxRange, Double> busMinCol, busMaxCol, busRateCol;
    @FXML private TableColumn<TaxRange, Void> busActionCol;

    private final TaxRangeService service = TaxRangeService.getInstance();

    @FXML
    public void initialize() {
        setupTable(govTable, govMinCol, govMaxCol, govRateCol, govActionCol, "salary_gov");
        setupTable(privateTable, privMinCol, privMaxCol, privRateCol, privActionCol, "salary_private_company");
        setupTable(businessTable, busMinCol, busMaxCol, busRateCol, busActionCol, "salary_business");
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
    private void addGovRange(ActionEvent event) {
        addRangeToTable(govTable, "salary_gov");
    }

    @FXML
    private void addPrivateRange(ActionEvent event) {
        addRangeToTable(privateTable, "salary_private_company");
    }

    @FXML
    private void addBusinessRange(ActionEvent event) {
        addRangeToTable(businessTable, "salary_business");
    }

    private void addRangeToTable(TableView<TaxRange> table, String category) {
        TaxRange newRange = new TaxRange();
        newRange.setCategory(category);
        if (service.addRange(category, 0, 0, 0)) {
            // Reload to get ID from DB
            table.setItems(service.getRanges(category));
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add new range.");
        }
    }

    @FXML
    private void saveAll(ActionEvent event) {
        int changes = 0;
        changes += saveTable(govTable);
        changes += saveTable(privateTable);
        changes += saveTable(businessTable);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Saved " + changes + " changes to database.");
    }

    private int saveTable(TableView<TaxRange> table) {
        int count = 0;
        for (TaxRange range : table.getItems()) {
            if (service.updateRange(range)) {
                count++;
                System.out.println("Updated range ID " + range.getId() + " in DB"); // Debug save
            } else {
                System.out.println("Failed to update range ID " + range.getId()); // Debug fail
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