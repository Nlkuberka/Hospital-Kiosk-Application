package application;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

import javafx.scene.image.ImageView;

public class UIControllerWM extends UIController{
    @FXML
    private JFXButton beginButton;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private void setBeginButton() {
        this.goToScene(UIController.PATHFINDING_MAIN);
    }

    public UIControllerWM() {

    }

    /**
     * Called when the scene is first created
     */
    @FXML
    public void initialize() {

        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
       backgroundImage.fitHeightProperty().bind(primaryStage.heightProperty());
    }
}
