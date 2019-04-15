package pathfinding;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIControllerPFCM extends UIController {
    @FXML
    private JFXButton startButton;
    @FXML
    private JFXButton destinationButton;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

    @FXML
    public void initialize() {
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
    }

    @FXML
    public void setStartButton(){

    }

    @FXML
    public void setDestinationButton(){

    }
}
