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
import helper.RoomCategoryFilterHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.util.*;

public class UIControllerSRSA extends UIController {
    String serviceType;
    RoomCategoryFilterHelper filterHelper;
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
    private ChoiceBox<String> roomCategory; /**< The room select dropdown*/

    @FXML
    private ChoiceBox<String> roomSelect; /**< The room select dropdown*/

    @FXML
    private ChoiceBox<String> sanitationSelect;

    @FXML
    public void initialize() {
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
    }

    @FXML
    public void onShow() {
        filterHelper = new RoomCategoryFilterHelper(roomCategory, roomSelect, null, false);

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
        String nodeID = filterHelper.getNodeID();
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
