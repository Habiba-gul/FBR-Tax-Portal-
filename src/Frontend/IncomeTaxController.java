package Frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class IncomeTaxController {

    @FXML
    public void handleSubmitTaxReturn() {
        System.out.println("Income Tax Return submitted! (Placeholder)");
    }

    @FXML
    public void handleBackToDashboard(ActionEvent event) {
        try {
            // Load the User Dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();

            // Get the current Stage and switch Scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Set the scene size back to the dashboard size
            Scene scene = new Scene(root, 800, 600); 
            
            stage.setScene(scene);
            stage.setTitle("FBR Tax Application - Dashboard");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load UserDashboard.fxml");
        }
    }

    
}
