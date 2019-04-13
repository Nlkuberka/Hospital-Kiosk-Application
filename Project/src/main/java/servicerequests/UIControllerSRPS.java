package servicerequests;

import application.CurrentUser;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Node;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextFormatter;
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
public class UIControllerSRPS extends UIController {
    String serviceType;
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/
    String[] prescriptionArray;
    String[] lengthArray = {"1 day", "1 week", "1 month", "3 months", "6 month", "1 year"};

    @FXML
    private ChoiceBox<String> roomSelect;

    @FXML
    private JFXTextArea serviceMessage;

    @FXML
    private JFXTextField patientNameTextField;

    @FXML
    private JFXTextField prescriptionTextField;

    @FXML
    private ChoiceBox<String> lengthChoiceBox;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    /**
     * Runs on initialize and adds the drugs to the textfield
     */
    @FXML
    public void initialize() {
        List<String> drugs = new LinkedList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/textfiles/prescriptiondrugs.txt"));
            String line = br.readLine();
            int count = 0;
            while((line = br.readLine()) != null) {
                String[] tokens = line.split("\\t");
                drugs.add(tokens[0]);
                count++;
            }
            prescriptionArray = new String[count];
            prescriptionArray = drugs.toArray(prescriptionArray);
            TextFields.bindAutoCompletion(prescriptionTextField, prescriptionArray);
        } catch(Exception e) {
            e.printStackTrace();
        }
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 50 ? e : null
        ));
        lengthChoiceBox.getItems().addAll(FXCollections.observableList(Arrays.asList(lengthArray)));
    }

    /**
     * Runs whenever the scene is shown and gets all of the room nodes
     */
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
        prescriptionTextField.setText("");
        patientNameTextField.setText("");
    }

    /**
     * Sets the service type
     * @param serviceType
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Sends the service request to the database
     */
    @FXML
    private void setConfirmButton() {
        String roomShortName = (String) roomSelect.getValue();
        String nodeID = nodeIDs.get(roomShortName);
        String message = prescriptionTextField.getText() + " for patient" + patientNameTextField.getText() + " for " + lengthChoiceBox.getValue() + "  " + serviceMessage.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.user.getUserID(), false, null);
        Connection conn = DBControllerSR.dbConnect();
        DBControllerSR.addServiceRequest(sr,conn);
        DBControllerSR.closeConnection(conn);
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    /**
     * Goes back to the service request main
     */
    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }
}
