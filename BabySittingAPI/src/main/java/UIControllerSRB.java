/**
 * The UIController for the babysitting service request
 * @author Shiyi Liu, imoralessirgo
 * @version iteration2
 */



import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIControllerSRB extends UIController {
    @FXML
    private ImageView backgroundImage;
    String serviceType;
    Map<String, String> nodeIDs; /**< Holds reference between node short name and nodeID*/
    @FXML private CheckBox feeder;
    @FXML private CheckBox diaper;
    @FXML private CheckBox bath;
    @FXML private CheckBox toy;
    @FXML private CheckBox babyStroller;
    @FXML private CheckBox food;
    String BabysittingServices = "";

    @FXML
    private ChoiceBox roomSelect; /**< The room choice box*/

    @FXML
    private JFXTextField serviceMessage; /**< The additional message field*/

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    private JFXButton cancelButton;/**< The cancel button*/

    @FXML
    public void initialize() {
//        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));

        setupRoomSelect();

    }

    @FXML
    public void onShow() {
        setupRoomSelect();
    }

    private void setupRoomSelect(){
        List<String> nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();

        Connection conn = DBControllerAPI.dbConnect();
        List<Node> nodes = DBControllerAPI.generateListOfNodes(conn,DBControllerAPI.ALL_ROOMS);
        DBControllerAPI.closeConnection(conn);
        for(int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            nodeShortNames.add(node.getShortName());
            nodeIDs.put(node.getShortName(), node.getNodeID());
            System.out.println(nodes.get(i));
        }

        roomSelect.setItems(FXCollections.observableList(nodeShortNames));
        roomSelect.getSelectionModel().selectFirst();
        serviceMessage.setText("");
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
        String nodeID = nodeIDs.get(roomShortName);
        setServiceType("Babysitting");
        checkEvent();
        String message = "Help with: "+ BabysittingServices + " Room: "+ roomShortName + serviceMessage.getText();
        if(message.length() >= 151){
            message = message.substring(0,149);
        }
        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, "DEFAULT", false, null);
        Connection conn = DBControllerAPI.dbConnect();
        DBControllerAPI.addServiceRequest(sr,conn);
        DBControllerAPI.closeConnection(conn);
        this.goToScene(UIController.SERVICE_REQUEST_MAIN,"");
    }

    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN,"");
    }
}
