package Frontend;

import Backend.Notification;
import Backend.NotificationDAO;
import Backend.SystemManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class NotificationsController {

    @FXML
    private TableView<Notification> notificationTable;
    @FXML
    private TableColumn<Notification, String> titleColumn;
    @FXML
    private TableColumn<Notification, String> messageColumn;
    @FXML
    private TableColumn<Notification, String> typeColumn;
    @FXML
    private TableColumn<Notification, Timestamp> dateColumn;

    private final ObservableList<Notification> notifications = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        loadNotifications();
    }

    private void loadNotifications() {
        notifications.clear();

        if (SystemManager.getCurrentUser() == null) {
            System.out.println("No user logged in");
            return;
        }

        int currentUserId = SystemManager.getCurrentUser().getId();
        List<Notification> list = NotificationDAO.getUserNotifications(currentUserId);
        notifications.addAll(list);

        notificationTable.setItems(notifications);

        if (list.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Notifications");
            alert.setHeaderText(null);
            alert.setContentText("You have no new notifications at this time.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadNotifications();
    }

    // NEW: Back to Dashboard
    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FBR Tax Portal - User Dashboard");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to return to dashboard.").show();
        }
    }
}