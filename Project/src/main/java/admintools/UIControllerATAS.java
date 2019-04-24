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
    private ChoiceBox<Integer> minuteCB;
    @FXML
    private ChoiceBox<Integer> secondCB;
    @FXML
    private Menu homeMenu;

    @FXML
    private void initialize() {
        // Add choices to the choice boxes.
        // Allow the admin to select an integer number of minutes and an integer number of seconds, up to an hour.
        for(int i = 0; i < 60; i += 10) {
            minuteCB.getItems().add(i);
            secondCB.getItems().add(i);
        }
    }

    @FXML
    public void onShow() {
        // Preset the choice boxes to the current timeout.
        int timeout = (int) (UIController.SESSION_TIMEOUT_THREAD.timeout / 1000);   // the time, in seconds, the application will wait for the user to do some action before logging them out
        minuteCB.setValue(timeout / 60);
        secondCB.setValue(timeout % 60);
    }

    @FXML
    private void  setConfirmButton() {
        // Change the internal timeout value to the time entered by the admin.
        long timeout = minuteCB.getValue() * 60 + secondCB.getValue();
        if(timeout != 0) {
            UIController.SESSION_TIMEOUT_THREAD.timeout = timeout * 1000;
            popupMessage("Timeout changed.", false);
        }
        else {
            popupMessage("Invalid timeout.", true);
        }
    }

}
