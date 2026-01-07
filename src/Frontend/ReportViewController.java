package Frontend;

import Backend.PaymentHistory;
import Backend.PaymentHistoryDAO;
import Backend.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportViewController {

    @FXML private Label totalPaymentsLabel;
    @FXML private ListView<String> paymentDatesList;

    private List<PaymentHistory> allHistory;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        paymentDatesList.setOnMouseClicked(event -> {
            String selected = paymentDatesList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openDetailView(selected);
            }
        });
    }

    public void setHistory(List<PaymentHistory> history) {
        this.allHistory = history;

        totalPaymentsLabel.setText("Total Payments: " + history.size());

        paymentDatesList.getItems().clear();
        for (PaymentHistory ph : history) {
            paymentDatesList.getItems().add(ph.getPaymentDate().format(formatter));
        }
    }

    private void openDetailView(String selectedDateStr) {
        PaymentHistory selected = allHistory.stream()
                .filter(ph -> ph.getPaymentDate().format(formatter).equals(selectedDateStr))
                .findFirst()
                .orElse(null);

        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportDetail.fxml"));
            Parent root = loader.load();

            ReportDetailController controller = loader.getController();
            controller.setPayment(selected);

            Stage detailStage = new Stage();
            detailStage.setTitle("FBR Tax Portal - Payment Details");
            detailStage.setScene(new Scene(root, 900, 700));
            detailStage.centerOnScreen();
            detailStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("FBR Tax Portal - User Dashboard");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}