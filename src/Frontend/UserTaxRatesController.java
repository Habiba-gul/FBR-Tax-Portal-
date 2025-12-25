package Frontend;

import java.io.IOException;

import Backend.TaxSlab;
import Backend.TaxSlabService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UserTaxRatesController {

    @FXML private TableView<TaxSlab> taxTable;
    @FXML private TableColumn<TaxSlab, String> typeCol;
    @FXML private TableColumn<TaxSlab, String> categoryCol;
    @FXML private TableColumn<TaxSlab, Double> minCol;
    @FXML private TableColumn<TaxSlab, Double> maxCol;
    @FXML private TableColumn<TaxSlab, Double> rateCol;
@FXML
private Button backBtn;

@FXML
private void handleBackToDashboard(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private final TaxSlabService slabService = new TaxSlabService();

    @FXML
    public void initialize() {
        typeCol.setCellValueFactory(new PropertyValueFactory<>("taxType"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        minCol.setCellValueFactory(new PropertyValueFactory<>("minAmount"));
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));

        taxTable.setItems(slabService.getAllSlabs());
        taxTable.setEditable(false); // 🔒 USER READ ONLY
    }
}
