import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * The UIController for the making and sending of service requests
 * @author Jonathan Chang
 * @verion iteration1
 */
public class UIControllerSRM extends UIController {
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/
    List<String> serviceTypes; /**< Hold the list of services*/

    @FXML
    private ChoiceBox<String> roomSelect; /**< The choicebox for node selection*/

    @FXML
    private ChoiceBox<String> serviceSelect; /**< The choicebox for service selection*/

    @FXML
    private TextArea serviceMessage; /**< The area for addition messages*/

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    /**
     * Runs on the scene creation and adds the various service request types
     */
    @FXML
    public void initialize() {
        serviceTypes = new ArrayList<String>();
        serviceTypes.add("Maintenance Request");
        serviceTypes.add("Tech Support Request");
        serviceSelect.setItems(FXCollections.observableList(serviceTypes));
    }

    /**
     * Runs whenever the scene is show and gets the node names for the choicebox
     */
    @Override
    public void onShow() {
        List<String> nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();

        // DB Get all Nodes

        roomSelect.setItems(FXCollections.observableList(nodeShortNames));
    }

    /**
     * Creates a new serviceRequest and sends it to the database
     */
    @FXML
    private void setConfirmButton() {
        String serviceType = (String) serviceSelect.getValue();
        String roomShortName = (String) nodeIDs.get((String) roomSelect.getValue());
        String nodeID = nodeIDs.get(roomShortName);
        String message = serviceMessage.getText();
        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.userID, false, "");
        //DB Add Service Request
        System.out.println(message);
    }
}
