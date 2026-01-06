package Frontend;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SplashController {

    @FXML
    private ImageView logoImage;

    @FXML
    public void initialize() {
        // Load the image safely
        logoImage.setImage(new Image(getClass().getResourceAsStream("fbrLogo.png")));
    }
}
