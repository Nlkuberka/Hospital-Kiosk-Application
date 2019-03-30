import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
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
    private Button sendServiceRequestButton;

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
    private void setSendServiceRequestButton() {
        String serviceType = (String) serviceSelect.getValue();
        String room = (String) nodeIDs.get((String) roomSelect.getValue());
        String message = serviceMessage.getText();
    }
}
