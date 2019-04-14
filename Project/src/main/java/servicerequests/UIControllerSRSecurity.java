package servicerequests;

import application.CurrentUser;
import database.DBController;
import application.UIController;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Node;

import com.jfoenix.controls.JFXButton;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.util.*;

/*
Security Service Request
 */
public class UIControllerSRSecurity extends UIController {
    String serviceType;
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/

    @FXML
    private ChoiceBox roomSelect;

    @FXML
    private ChoiceBox prioritySelect;

    @FXML
    private TextArea serviceMessage;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    private JFXButton cancelButton;

    @FXML
    private ImageView backgroundImage;

    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
    }

    @FXML
    public void onShow() {
        List<String> nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();


        Connection conn = DBControllerNE.dbConnect();
        LinkedList<Node> rooms = DBControllerNE.generateListOfNodes(conn,DBControllerNE.ALL_ROOMS);
        for(Node n:rooms) {
            nodeIDs.put(n.getShortName(), n.getNodeID());
            nodeShortNames.add(n.getShortName());
        }
        DBControllerNE.closeConnection(conn);
        roomSelect.setItems(FXCollections.observableList(nodeShortNames));
        roomSelect.getSelectionModel().selectFirst();

        String[] priorities = {"1","2","3","4","5"};
        prioritySelect.setItems(FXCollections.observableList(Arrays.asList(priorities)));
        prioritySelect.getSelectionModel().selectFirst();

        serviceMessage.setText("");
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        String roomShortName = (String) roomSelect.getValue();
        String nodeID = nodeIDs.get(roomShortName);
        String message = prioritySelect.getValue() + " - " + serviceMessage.getText();
        serviceMessage.clear();

        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.user.getUserID(), false, null);
        Connection conn = DBControllerSR.dbConnect();
        DBControllerSR.addServiceRequest(sr,conn);
        DBControllerSR.closeConnection(conn);
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }
}
