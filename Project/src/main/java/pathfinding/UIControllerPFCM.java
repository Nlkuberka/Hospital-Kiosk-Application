package pathfinding;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIControllerPFCM extends UIController {
    public Text text;
    @FXML
    private JFXButton startButton;
    @FXML
    private JFXButton destinationButton;

    private UIControllerPFM pfm;
    private String nodeLongName;

    public void setNodeLongName(String longName) {
        this.nodeLongName = longName;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void setStartButton(){
        this.pfm.setInitialLocation(this.nodeLongName);
    }

    @FXML
    public void setDestinationButton(){
        this.pfm.setDestinationLocation(this.nodeLongName);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setController(UIControllerPFM controller) {
        this.pfm = controller;
    }
}
