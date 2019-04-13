package servicerequests;

import application.CurrentUser;
import application.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.Map;

/**
 * IT Service Request
 * @author imoralessirgo
 */

public class UIControllerSRIT extends UIController{
    @FXML
    private ImageView backgroundImage;
    String serviceType;
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/
    ObservableList<String> workplaceNum = FXCollections.observableArrayList();
    String[] ITservices = {"Multimedia","Audio","Computer","Projector","Locked out of server","Lighting","Remote control malfunction",
                            "Network connection","Required software","Conference Call","Microphone","Other"};


    @FXML
    private ChoiceBox roomNum;

    @FXML
    private JFXTextField serviceRequired;

    @FXML
    private JFXTextField Notes;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/


    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        TextFields.bindAutoCompletion(serviceRequired,ITservices);
        roomNum.getItems().addAll("Classroom 1","Classroom 2","Classroom 3","Classroom 4","Classroom 5",
                "Classroom 6","Classroom 7","Classroom 8","Workzone 1","Workzone 2","Workzone 3","Workzone 4",
                "Workzone 5","Mission Hill Conference Room","MissionHillAuditorium");
    }


    @FXML
    public void onShow() {

    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        String nodeID = null;
        String ITService = serviceRequired.getText();
        String room = (String) roomNum.getValue();
        String Addmessage = Notes.getText();
        setServiceType("IT");
        String message = "Help with: "+ ITService + " Room: "+ room +" "+Addmessage+" ";
        if(message.length() >= 151){
            message = message.substring(0,149);
        }
        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.user.getUserID(), false, null);
        //System.out.println(sr);
        Connection conn = DBController.dbConnect();
        sr.setServiceID(DBController.addServiceRequest(sr,conn));
        DBController.closeConnection(conn);

        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

}
