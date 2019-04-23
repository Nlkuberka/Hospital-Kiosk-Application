package servicerequests;

import application.CurrentUser;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import database.DBController;
import database.DBControllerSR;
import entities.ServiceRequest;
import helper.ServiceRequestTableHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static pathfinding.UIControllerPUD.ACCOUNT_SID;
import static pathfinding.UIControllerPUD.AUTH_TOKEN;

public class UIControllerURS extends UIController {
    private static final String[] serviceRequestSetters  = {"", "", "", "setResolved", "setResolverID", ""};
    private static final String[] serviceRequestGetters  = {"getNodeID", "getServiceType", "getUserID", "isResolved", "getResolverID", "getMessage"};
    private static final Map<String, String> serviceMap = new HashMap<String, String>() {{
        put("Religious Services", DBControllerSR.TYPE_RELIGIOUS_SERVICES);
        put("Flower Delivery", DBControllerSR.TYPE_FLOWER_DELIVERY);
        put("Babysitter", DBControllerSR.TYPE_BABYSITTER);
        put("Other", DBControllerSR.TYPE_OTHER);
    }};

    @FXML
    private ImageView backgroundImage;
    /**< The Various servicerequests Columns used for cell factories */
    @FXML
    private MenuItem backButton; /**< The Back Button */

    @FXML
    private Menu homeButton; /**< The Home Button */

    @FXML
    private TableView<ServiceRequest> serviceRequestTable; /**< The table that holds all of the nodes */

    /**
     * Called when the scene is first created
     * Sets up all the cell factories
     */
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        ObservableList<TableColumn<ServiceRequest, ?>> tableColumns = serviceRequestTable.getColumns();

        new ServiceRequestTableHelper(serviceRequestTable,
                (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(0),
                (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(1),
                (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(2),
                (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(3),
                (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(4),
                (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(5),
                (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(6));
    }

    /**
     * Run when the scene is shown
     */
    @Override
    public void onShow() {
        Connection conn = DBController.dbConnect();
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        List<String> serviceRequestTypes = CurrentUser.user.getServiceRequestFullfillment();
        for(String serviceRequestType : serviceRequestTypes) {
            serviceRequests.addAll(DBControllerSR.getServiceRequests(serviceMap.get(serviceRequestType), conn));
        }
        serviceRequestTable.setItems(FXCollections.observableList(serviceRequests));
    }

    /**
     * Goes back to the admin tools application menu
     */
    @FXML
    private void setBackButton() {
        this.goToScene(UIController.PATHFINDING_MAIN);
    }
}
