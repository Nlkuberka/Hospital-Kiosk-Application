package admintools;

import application.UIController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ATMVAddNodePopup extends UIController {
    public StackPane parentPane;
    public ImageView backgroundImage;

    @FXML
    public void initialize()
    {
        backgroundImage.setFitHeight(400);
        backgroundImage.setFitWidth(600);
    }
}
