package edu.wpi.cs3733.d19.teamD;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Handles the simple controller for the popup warning window
 * @author Jonathan Chang
 * @version iteration2
 */
public class UIControllerPUM extends UIController {

    @FXML
    private JFXButton okButton; /**< The ok button to close the window*/

    @FXML
    private Label warningLabel; /**< The warning label to display the warning message*/

    @FXML
    public void initialize() {

    }

    /**
     * Sets the warning label to the given string
     * @param message The warning string to display to the user
     */
    public void setMessage(String message) {
        warningLabel.setText(message);
    }

    /**
     * Closes the window and sends the user back to the main application window
     */
    @FXML
    private void setOkButton() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
