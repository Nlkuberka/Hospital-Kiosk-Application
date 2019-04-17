package servicerequests;

import application.CurrentUser;
import com.jfoenix.controls.JFXTextField;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import database.DBControllerNE;
import database.DBControllerSR;
import entities.Node;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class UIControllerSRFD extends UIController {
    @FXML
    private ImageView backgroundImage;
    String flowerDelivery;
    private RoomCategoryFilterHelper filterHelper;

    @FXML ComboBox<String> roomSelect;

    @FXML
    private JFXTextArea serviceMessage1;

    @FXML
    private JFXTextField phoneNum;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    private Label costLabel;



    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        serviceMessage1.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
    }

    @FXML
    public void onShow() {
        filterHelper = new RoomCategoryFilterHelper(roomSelect, null, true);
        serviceMessage1.setText("");
    }

    public void setServiceType(String serviceType) {
        this.flowerDelivery = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        String roomShortName = (String) roomSelect.getValue();
        String nodeID = filterHelper.getNodeID();
        String message = serviceMessage1.getText();
        String phoneNumber = phoneNum.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, flowerDelivery, phoneNumber + message + costLabel.getText(), CurrentUser.user.getUserID(), false, null);
        System.out.println(sr.toString());
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
    public void costPic1() {
        costLabel.setText("$20.00");
    }

    @FXML
    public void costPic2() {
        costLabel.setText("$15.00");
    }
    @FXML
    public void costPic3() {
        costLabel.setText("$30.00");
    }
    @FXML
    public void costPic4() {
        costLabel.setText("$13.00");
    }
    @FXML
    public void costPic5() {
        costLabel.setText("$25.00");
    }
    @FXML
    public void costPic6() {
        costLabel.setText("$29.00");
    }
    @FXML
    public void costPic7() {
        costLabel.setText("$23.00");
    }
    @FXML
    public void costPic8() {
        costLabel.setText("$10.00");
    }
    @FXML
    public void costPic9() {
        costLabel.setText("$5.00");
    }
}
