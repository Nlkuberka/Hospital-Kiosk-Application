package servicerequests;

import application.CurrentUser;
import application.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import entities.Edge;
import entities.Graph;
import entities.Node;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private ChoiceBox<String> roomSelect = new ChoiceBox<String>(); /**< The room select dropdown*/

    @FXML
    public void initialize() {
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
    }

    @FXML
    public void onShow() {
        Connection conn = DBController.dbConnect();
        List<String> nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();

        // DB Get all Nodes
        try {
            ResultSet rs = conn.createStatement().executeQuery("Select * From NODES"); //can select from all nodes
            while (rs.next()) {
                nodeIDs.put(rs.getString("SHORTNAME"), rs.getString("NODEID"));
                nodeShortNames.add(rs.getString("SHORTNAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObservableList<String> content = FXCollections.observableArrayList();
        int index = 0;
        for(String nodeID : nodeIDs.values()){
            System.out.println(nodeID);
            content.add(index, nodeID);
            index++;
        }

        roomSelect.getItems().addAll(content);
        serviceMessage.setText("");


    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        Connection connection = DBController.dbConnect();
        String roomShortName = DBController.IDfromLongName(roomSelect.getValue(), connection);
        String nodeID = nodeIDs.get(roomShortName);
        String message = serviceMessage.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.user.getUserID(), false, null);
        Connection conn = DBController.dbConnect();
        DBController.addServiceRequest(sr,conn);
        DBController.closeConnection(conn);

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
    private void setRoomSelect(){
    }
}
