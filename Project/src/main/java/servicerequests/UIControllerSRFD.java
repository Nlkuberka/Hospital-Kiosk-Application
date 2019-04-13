package servicerequests;

import application.CurrentUser;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Node;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class UIControllerSRFD extends UIController {
    String flowerDelivery;
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/

    @FXML
    private ChoiceBox roomSelect;

    @FXML
    private JFXTextArea serviceMessage1;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    private Label costLabel;



    @FXML
    public void initialize() {
        serviceMessage1.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
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
        serviceMessage1.setText("");
    }

    public void setServiceType(String serviceType) {
        this.flowerDelivery = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        String roomShortName = (String) roomSelect.getValue();
        String nodeID = nodeIDs.get(roomShortName);
        String message = serviceMessage1.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, flowerDelivery, message + costLabel.getText(), CurrentUser.user.getUserID(), false, null);
        System.out.println(sr.toString());
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
    public void costPic1() {
        costLabel.setText("$20.00");
    }

    @FXML
    public void costPic2() {
        costLabel.setText("$15.00");
    }
    @FXML
    public void costPic3() {
        costLabel.setText("$30.00");
    }
    @FXML
    public void costPic4() {
        costLabel.setText("$13.00");
    }
    @FXML
    public void costPic5() {
        costLabel.setText("$25.00");
    }
    @FXML
    public void costPic6() {
        costLabel.setText("$29.00");
    }
    @FXML
    public void costPic7() {
        costLabel.setText("$23.00");
    }
    @FXML
    public void costPic8() {
        costLabel.setText("$10.00");
    }
    @FXML
    public void costPic9() {
        costLabel.setText("$5.00");
    }
}
