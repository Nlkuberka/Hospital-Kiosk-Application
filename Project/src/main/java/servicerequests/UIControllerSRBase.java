package servicerequests;

import application.CurrentUser;
import application.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import org.omg.CORBA.Current;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIControllerSRBase extends UIController {
    String serviceType;
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/

    @FXML
    private ChoiceBox roomSelect;

    @FXML
    private TextArea serviceMessage;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    public void initialize() {
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
    }

    @FXML
    public void onShow() {
        List<String> nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();

        // DB Get all Nodes
        try {
            Connection conn = DBController.dbConnect();
            ResultSet rs = conn.createStatement().executeQuery("Select * From NODES where FLOOR = '2' AND BUILDING = 'Tower'");
            while (rs.next()) {
                nodeIDs.put(rs.getString("SHORTNAME"), rs.getString("NODEID"));
                nodeShortNames.add(rs.getString("SHORTNAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        String roomShortName = (String) roomSelect.getValue();
        String nodeID = nodeIDs.get(roomShortName);
        String message = serviceMessage.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.user.getUserID(), false, null);
        Connection conn = DBController.dbConnect();
        DBController.addServiceRequest(sr,conn);
        DBController.closeConnection(conn);
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }
}
