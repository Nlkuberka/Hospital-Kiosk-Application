package servicerequests;

import application.CurrentUser;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Node;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIControllerSRIN extends UIController {
    @FXML
    private ImageView backgroundImage;
    String serviceType;
    RoomCategoryFilterHelper filterHelper;

    @FXML
    private ChoiceBox<String> roomCategory;

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
        filterHelper = new RoomCategoryFilterHelper(roomCategory, roomSelect, null, true);

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
        String nodeID = filterHelper.getNodeID();
        String message = "Language: " + languageSelect.getValue() + " Comments: " + serviceMessage.getText();
        message = message.substring(0, Math.min(150, message.length()));

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
