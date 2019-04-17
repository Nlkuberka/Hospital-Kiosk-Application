package servicerequests;

import application.CurrentUser;
import com.jfoenix.controls.JFXComboBox;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Node;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIControllerSRAVE extends UIController {
    public GridPane gridPane;
    public ChoiceBox serviceSelect;
    String serviceType;
    private RoomCategoryFilterHelper filterHelper;

    @FXML
    private JFXComboBox<String> roomSelect;

    @FXML
    private JFXTextField serviceMessage;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    private ImageView backgroundImage;

    @FXML
    public void initialize() {
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));

        serviceSelect.getItems().addAll("Computer", "Projector", "Speakers", "Display", "Microphone");

        gridPane.setFillHeight(serviceMessage, true);
        gridPane.setFillWidth(serviceMessage, true);

        confirmButton.setDisable(true);

            backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());


    }

    @FXML
    public void onShow() {
        filterHelper = new RoomCategoryFilterHelper(roomSelect, null, true);
        serviceMessage.setText("");
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        String roomShortName = roomSelect.getValue().toString();
        String service = serviceSelect.getValue().toString();
        String nodeID = filterHelper.getNodeID();
        String message = serviceMessage.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, "Equipment Needed: " + service + "\n Message: " + message, CurrentUser.user.getUserID(), false, null);
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
    private void validate(ActionEvent actionEvent) {
        if (serviceSelect.getValue() != null && roomSelect.getValue() != null && serviceSelect.getValue().toString().length() > 0 && roomSelect.getValue().toString().length() > 0)
            confirmButton.setDisable(false);
    }
}
