package admintools;

import application.SessionTimeoutThread;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class UIControllerATAS extends UIController {
    @FXML
    private JFXButton confirmButton;
    @FXML
    private ChoiceBox minutCB;
    @FXML
    private ChoiceBox secondCB;
    @FXML
    private Label minutLabel;
    @FXML
    private Label secondLabel;
    @FXML
    private Menu homeMenu;
    @FXML
    private MenuItem backMenuItem;

    @FXML
    public void initialize() {

    }

    @FXML
    public void onShow() {
        long timeout = SessionTimeoutThread.timeout;
        long seconds = timeout / 1000;
        minutLabel.setText(seconds / 60 + "min");
        secondLabel.setText(seconds % 60 + "s");
    }

    @FXML
    public void setBackMenuItem() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }

    @FXML
    public void setConfirmButton() {
        
    }
}
