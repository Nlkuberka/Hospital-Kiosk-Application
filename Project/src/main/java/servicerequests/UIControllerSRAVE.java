package servicerequests;

import application.CurrentUser;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Node;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIControllerSRAVE extends UIController {
    public GridPane gridPane;
    public ChoiceBox serviceSelect;
    String serviceType;
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/

    @FXML
    private ChoiceBox roomSelect;

    @FXML
    private JFXTextField serviceMessage;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    public void initialize() {
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));

        serviceSelect.getItems().addAll("Computer", "Projector", "Speakers", "Display", "Microphone");

        gridPane.setFillHeight(serviceMessage, true);
        gridPane.setFillWidth(serviceMessage, true);

        confirmButton.setDisable(true);
    }

    @FXML
    public void onShow() {
        List<String> nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();

        Connection conn = DBControllerNE.dbConnect();
        List<Node> nodes = DBControllerNE.generateListOfNodes(conn,DBControllerNE.ALL_ROOMS);
        DBControllerNE.closeConnection(conn);
        for(int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            nodeShortNames.add(node.getShortName());
            nodeIDs.put(node.getShortName(), node.getNodeID());
        }

        roomSelect.setItems(FXCollections.observableList(nodeShortNames));
        roomSelect.getSelectionModel().selectFirst();
        serviceMessage.setText("");
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        String roomShortName = roomSelect.getValue().toString();
        String service = serviceSelect.getValue().toString();
        String nodeID = nodeIDs.get(roomShortName);
        String message = serviceMessage.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, "Equipment Needed: " + service + "\n Message: " + message, CurrentUser.user.getUserID(), false, null);
        Connection conn = DBControllerSR.dbConnect();
        DBControllerSR.addServiceRequest(sr,conn);
        DBControllerSR.closeConnection(conn);
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    @FXML
    private void validate(ActionEvent actionEvent) {
        if (serviceSelect.getValue() != null && roomSelect.getValue() != null && serviceSelect.getValue().toString().length() > 0 && roomSelect.getValue().toString().length() > 0)
            confirmButton.setDisable(false);
    }
}
