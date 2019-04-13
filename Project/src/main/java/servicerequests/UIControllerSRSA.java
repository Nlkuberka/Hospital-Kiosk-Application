package servicerequests;

import application.CurrentUser;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Graph;
import entities.Node;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.util.*;

public class UIControllerSRSA extends UIController {
    String serviceType;
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/
    private Graph graph;

    @FXML
    private TextField serviceMessage;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    private JFXButton cancelButton; /**< The cancel button*/

    @FXML
    private Menu homeButton; /**< The home button*/

    @FXML
    private ChoiceBox<String> roomSelect; /**< The room select dropdown*/

    @FXML
    private ChoiceBox<String> sanitationSelect;

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
        Connection conn = DBControllerNE.dbConnect();
        List<String> nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();

        List<Node> nodes = DBControllerNE.generateListOfNodes(conn,DBControllerNE.ALL_ROOMS);
        System.out.println(nodes.size());
        DBControllerNE.closeConnection(conn);
        for(int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            nodeShortNames.add(node.getShortName());
            nodeIDs.put(node.getShortName(), node.getNodeID());
        }

        roomSelect.setItems(FXCollections.observableList(nodeShortNames));

        serviceMessage.setText("");

        sanitationSelect.getItems().addAll("Fecal Matter", "Vomit", "Urine", "Blood", "Water", "Soda", "Juice");
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        Connection connection = DBControllerSR.dbConnect();
        String roomShortName = DBControllerSR.IDfromLongName(roomSelect.getValue(), connection);
        String nodeID = nodeIDs.get(roomShortName);
        String message = serviceMessage.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.user.getUserID(), false, null);
        Connection conn = DBController.dbConnect();
        DBControllerSR.addServiceRequest(sr,conn);
        DBControllerSR.closeConnection(conn);

        serviceMessage.setText("Thank you! We will get on this soon!");
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    @FXML
    private void setHomeButton() {
        this.goToScene(UIController.LOGIN_MAIN);
    }

    @FXML
    private void setRoomSelect(){}

    @FXML
    private void setSanitationSelect(){}
}
