package servicerequests;

import application.CurrentUser;
import application.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.Node;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIControllerSRIN extends UIController {
    @FXML
    private ImageView backgroundImage;
    String serviceType;
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/

    @FXML
    private ChoiceBox<String> roomSelect;

    @FXML
    private ChoiceBox<String> languageSelect;

    @FXML
    private JFXTextField serviceMessage;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        languageSelect.getItems().addAll("Spanish", "Mandarin", "Arabic", "French");
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
    }

    @SuppressWarnings("Duplicates")
    @FXML
    public void onShow() {
        nodeIDs = new HashMap<>();

        // DB Get all Nodes
        Connection conn = DBController.dbConnect();
        System.out.println(conn);
        List<Node> rooms = DBController.fetchAllRooms(conn);
        for(Node room : rooms) {
            roomSelect.getItems().add(room.getShortName());
            nodeIDs.put(room.getShortName(), room.getNodeID());
        }
        //roomSelect.setItems(FXCollections.observableList(nodeShortNames));
        roomSelect.getSelectionModel().selectFirst();
        languageSelect.getSelectionModel().selectFirst();
        serviceMessage.setText("");

    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void setConfirmButton() {
        String roomShortName = roomSelect.getValue();
        String nodeID = nodeIDs.get(roomShortName);
        String message = "Language: " + languageSelect.getValue() + " Comments: " + serviceMessage.getText();
        message = message.substring(0, Math.min(150, message.length()));

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
