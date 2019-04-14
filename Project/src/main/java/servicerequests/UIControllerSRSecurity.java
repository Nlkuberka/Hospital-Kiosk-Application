package servicerequests;

import application.CurrentUser;
import database.DBController;
import application.UIController;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Node;

import com.jfoenix.controls.JFXButton;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;

import java.sql.Connection;
import java.util.*;

/*
Security Service Request
 */
public class UIControllerSRSecurity extends UIController {
    String serviceType;
    RoomCategoryFilterHelper filterHelper;

    @FXML
    private ChoiceBox roomCategory;

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
    public void initialize() {
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
    }

    @FXML
    public void onShow() {
        filterHelper = new RoomCategoryFilterHelper(roomCategory, roomSelect, null, false);

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
        String nodeID = filterHelper.getNodeID();
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
