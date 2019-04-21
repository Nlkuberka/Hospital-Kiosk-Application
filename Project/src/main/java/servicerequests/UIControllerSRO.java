package servicerequests;

import application.CurrentUser;
import com.jfoenix.controls.JFXComboBox;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Node;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import org.controlsfx.control.textfield.TextFields;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.*;

/**
 * UIController for the prescription services service request
 * @author Jonathan Chang
 * @version iteration2
 */
public class UIControllerSRO extends UIController {
    @FXML
    private ImageView backgroundImage;

    private RoomCategoryFilterHelper filterHelper;

    @FXML
    private JFXComboBox<String> roomSelect;

    @FXML
    private JFXTextArea serviceMessage;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXTextField serviceTypeTextField;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    /**
     * Runs on initialize and adds the drugs to the textfield
     */
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 50 ? e : null
        ));
    }

    /**
     * Runs whenever the scene is shown and gets all of the room nodes
     */
    @FXML
    public void onShow() {
        roomSelect.getSelectionModel().clearSelection();
        filterHelper = new RoomCategoryFilterHelper(roomSelect, null, true);

        serviceMessage.setText("");
        nameTextField.setText("");
        serviceTypeTextField.setText("");
    }

    /**
     * Sends the service request to the database
     */
    @FXML
    private void setConfirmButton() {
        String nodeID = filterHelper.getNodeID();
        String message = nameTextField.getText() + " requests " +serviceTypeTextField.getText() + " at " + filterHelper.getLongName() + " : " + serviceMessage.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, "Other", message, CurrentUser.user.getUserID(), false, null);
        Connection conn = DBControllerSR.dbConnect();
        DBControllerSR.addServiceRequest(sr,conn);
        DBControllerSR.closeConnection(conn);
        this.goToScene(UIController.PATHFINDING_MAIN);
    }

    /**
     * Goes back to the service request main
     */
    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.PATHFINDING_MAIN);
    }
}
