package servicerequests;

import application.CurrentUser;
import application.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.controlsfx.control.textfield.TextFields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.server.ExportException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    @FXML
    public void onShow() {
        List<String> nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();

        // DB Get all Nodes
        try {
            Connection conn = DBController.dbConnect();
            ResultSet rs = conn.createStatement().executeQuery("Select * From NODES");
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
        prescriptionTextField.setText("");
        patientNameTextField.setText("");
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        String roomShortName = (String) roomSelect.getValue();
        String nodeID = nodeIDs.get(roomShortName);
        String message = prescriptionTextField.getText() + " for patient" + patientNameTextField.getText() + " for " + lengthChoiceBox.getValue() + "  " + serviceMessage.getText();

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
