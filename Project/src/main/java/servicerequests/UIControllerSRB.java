package servicerequests;

/**
 * The UIController for the babysitting service request
 * @author Shiyi Liu
 * @version iteration2
 */

import application.CurrentUser;
import com.jfoenix.controls.JFXComboBox;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DBControllerSR;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.sql.Connection;

public class UIControllerSRB extends UIController {
    String serviceType;
    private RoomCategoryFilterHelper filterHelper;
    @FXML private CheckBox feeder;
    @FXML private CheckBox diaper;
    @FXML private CheckBox bath;
    @FXML private CheckBox toy;
    @FXML private CheckBox babyStroller;
    @FXML private CheckBox food;
    String BabysittingServices = "";

    @FXML
    private JFXComboBox<String> roomSelect; /**< The room choice box*/

    @FXML
    private JFXTextField serviceMessage; /**< The additional message field*/

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    private JFXButton cancelButton;/**< The cancel button*/

    @FXML
    public void initialize() {
        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
    }

    @FXML
    public void onShow() {
        roomSelect.getSelectionModel().clearSelection();
        filterHelper = new RoomCategoryFilterHelper(roomSelect, null, false);
        serviceMessage.setText("");
        feeder.setSelected(false);
        diaper.setSelected(false);
        bath.setSelected(false);
        toy.setSelected(false);
        babyStroller.setSelected(false);
        food.setSelected(false);
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void checkEvent(){
        if(feeder.isSelected()){
            BabysittingServices += "Feeder Service, ";
        }

        if(diaper.isSelected()){
            BabysittingServices += "Diaper Changing Service, ";
        }

        if(bath.isSelected()){
            BabysittingServices += "Bath Service, ";
        }

        if(toy.isSelected()){
            BabysittingServices += "Toy Service, ";
        }

        if(babyStroller.isSelected()){
            BabysittingServices += "Baby Stroller Borrowing Service, ";

        }

        if(food.isSelected()){
            BabysittingServices += "Children Food Delivery Service, ";
        }

    }

    @FXML
    private void setConfirmButton() {
        String roomShortName = (String) roomSelect.getValue();
        String nodeID = filterHelper.getNodeID();
        setServiceType("Babysitting");
        checkEvent();
        String message = "Help with: "+ BabysittingServices + " Room: "+ roomShortName + serviceMessage.getText();
        if(message.length() >= 151){
            message = message.substring(0,149);
        }
        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.user.getUserID(), false, null);
        sr.setServiceID(sr.getTimeStamp());
        System.out.println(sr.getServiceID());
        Connection conn = DBControllerSR.dbConnect();
        DBControllerSR.addServiceRequest(sr,conn);
        DBControllerSR.closeConnection(conn);
        setCancelButton();
    }

    @FXML
    private void setCancelButton() {
        Stage stage = (Stage) roomSelect.getScene().getWindow();
        stage.close();
    }
}
