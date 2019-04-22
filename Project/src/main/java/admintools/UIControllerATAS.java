package admintools;

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
    private ChoiceBox<Integer> minutCB;
    @FXML
    private ChoiceBox<Integer> secondCB;
    @FXML
    private Label minutLabel;
    @FXML
    private Label secondLabel;
    @FXML
    private Menu homeMenu;
    @FXML
    private MenuItem backMenuItem;

    @FXML
    private void initialize() {
        int timeout = (int) (UIController.SESSION_TIMEOUT_THREAD.timeout / 1000);
        minutLabel.setText("" + (timeout / 60));
        secondLabel.setText("" + (timeout % 60));
        for(int i = 0; i < 60; i++) {
            minutCB.getItems().add(i);
            secondCB.getItems().add(i);
        }
    }

    @FXML
    private void  setConfirmButton() {
        long timeout = minutCB.getValue() * 60 + secondCB.getValue();
        if(timeout >= 5) {
            UIController.SESSION_TIMEOUT_THREAD.timeout = timeout * 1000;
        }
    }

    @FXML
    private void setBackMenuItem() {

    }
}
