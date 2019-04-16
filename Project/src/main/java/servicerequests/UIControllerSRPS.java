package servicerequests;

import application.CurrentUser;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import database.DBControllerSR;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import org.controlsfx.control.textfield.TextFields;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * UIController for the prescription services service request
 * @author Jonathan Chang
 * @version iteration2
 */
public class UIControllerSRPS extends UIController {
    @FXML
    private ImageView backgroundImage;
    String serviceType;
    String[] prescriptionArray;
    String[] lengthArray = {"1 day", "1 week", "1 month", "3 months", "6 month", "1 year"};
    private RoomCategoryFilterHelper filterHelper;

    @FXML
    private ChoiceBox<String> roomCategory;

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
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

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
        filterHelper = new RoomCategoryFilterHelper(roomCategory, roomSelect, null, true);

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
        String nodeID = filterHelper.getNodeID();
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
