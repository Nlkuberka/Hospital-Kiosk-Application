package servicerequests;

import application.CurrentUser;
import com.jfoenix.controls.JFXTextField;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import database.DBControllerSR;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.sql.Connection;

public class UIControllerSRFD extends UIController {
    String flowerDelivery;
    private RoomCategoryFilterHelper filterHelper;

    @FXML private JFXComboBox<String> roomSelect;

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
        //backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        serviceMessage1.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
    }

    @FXML
    public void onShow() {
        roomSelect.getSelectionModel().clearSelection();
        phoneNum.setText("");
        filterHelper = new RoomCategoryFilterHelper(roomSelect, null, true);
        serviceMessage1.setText("");
    }

    public void setServiceType(String serviceType) {
        this.flowerDelivery = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        String nodeID = filterHelper.getNodeID();
        String message = serviceMessage1.getText();
        String phoneNumber = phoneNum.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, flowerDelivery, phoneNumber + message + costLabel.getText(), CurrentUser.user.getUserID(), false, null);

        Connection conn = DBControllerSR.dbConnect();
        DBControllerSR.addServiceRequest(sr,conn);
        DBControllerSR.closeConnection(conn);
        setCancelButton();
    }

    @FXML
    private void setCancelButton() {
        Stage stage = (Stage) phoneNum.getScene().getWindow();
        stage.close();
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
