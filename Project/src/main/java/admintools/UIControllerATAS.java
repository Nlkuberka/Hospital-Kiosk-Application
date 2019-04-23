package admintools;

import application.SessionTimeoutThread;
import application.UIController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.LinkedList;

public class UIControllerATAS extends UIController {
    @FXML
    private ChoiceBox<Integer> minutCB;
    @FXML
    private ChoiceBox<Integer> secondCB;
    @FXML
    private Label minutLabel;
    @FXML
    private Label secondLabel;

    @FXML
    public void initialize() {
        minutCB.setItems(FXCollections.observableList(new LinkedList<Integer>(){{
            add(00); add(1); add(2); add(3); add(4); add(5);
        }}));
        secondCB.setItems(FXCollections.observableList(new LinkedList<Integer>(){{
            add(00); add(10); add(20); add(30); add(40); add(50);
        }}));
    }

    /**
     * Sets up the labels for the timeout
     */
    @FXML
    public void onShow() {
        long timeout = SessionTimeoutThread.timeout;
        long seconds = timeout / 1000;
        minutLabel.setText(seconds / 60 + " min");
        secondLabel.setText(seconds % 60 + " sec");
    }

    /**
     * Goes back to the admin tools main
     */
    @FXML
    public void setBackMenuItem() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }

    /**
     * Changes the timeout of the algorithm
     */
    @FXML
    public void setConfirmButton() {
        int minutes = minutCB.getSelectionModel().getSelectedItem();
        int seconds = secondCB.getSelectionModel().getSelectedItem();
        long milli = (minutes * 60 + seconds) * 1000;
        if(milli == 0) {
            popupMessage("Timeout cannot be 0", true);
            return;
        }
        SessionTimeoutThread.timeout = milli;
        onShow();
    }
}
