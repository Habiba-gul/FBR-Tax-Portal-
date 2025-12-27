package Frontend;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class TaxReceiptController {  // Class name matches fx:controller

    @FXML private TableView<ReceiptItem> receiptTable;
    @FXML private TableColumn<ReceiptItem, String> descCol;
    @FXML private TableColumn<ReceiptItem, Double> priceCol;
    @FXML private TableColumn<ReceiptItem, Integer> qtyCol;
    @FXML private TableColumn<ReceiptItem, Double> taxCol;
    @FXML private Label totalTaxLabel;

    @FXML
    public void initialize() {
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        taxCol.setCellValueFactory(new PropertyValueFactory<>("tax"));
    }

    public void setReceiptData(List<ReceiptItem> items, double totalTax) {
        receiptTable.setItems(FXCollections.observableArrayList(items));
        totalTaxLabel.setText(String.format("Total Tax: %.2f PKR", totalTax));
    }

    @FXML
    private void closeReceipt(ActionEvent event) {
        Stage stage = (Stage) receiptTable.getScene().getWindow();
        stage.close();
    }
}