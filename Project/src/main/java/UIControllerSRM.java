import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

class UIControllerSRM extends UIController {
    List<String> nodeShortNames;
    Map<String, String> nodeIDs;

    List<String> serviceTypes;

    @FXML
    private ChoiceBox roomSelect;

    @FXML
    private ChoiceBox serviceSelect;

    @FXML
    private TextArea serviceMessage;

    @FXML
    private JFXButton confirmButton;

    @FXML
    public void initialize() {
        nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();
        serviceTypes = new ArrayList<String>();
        serviceTypes.add("Maintenance Request");
        serviceTypes.add("Tech Support Request");

        // DB Get all Nodes

        roomSelect.setItems(FXCollections.observableArrayList(nodeShortNames.toArray()));
        serviceSelect.setItems(FXCollections.observableArrayList(serviceTypes.toArray()));
    }

    @FXML
    private void setConfirmButton() {
        String serviceType = (String) serviceSelect.getValue();
        String roomShortName = (String) nodeIDs.get((String) roomSelect.getValue());
        String nodeID = nodeIDs.get(roomShortName);
        String message = serviceMessage.getText();
        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.userID, false);
        //DB Add Service Request
    }
}
