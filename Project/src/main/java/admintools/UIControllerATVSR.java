package admintools;

import application.CurrentUser;
import com.jfoenix.controls.JFXButton;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import database.DBController;
import application.UIController;
import database.DBControllerSR;
import entities.ServiceRequest;

import com.jfoenix.controls.JFXCheckBox;

import helper.ServiceRequestTableHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static pathfinding.UIControllerPUD.ACCOUNT_SID;
import static pathfinding.UIControllerPUD.AUTH_TOKEN;

/**
 * The UIController for the viewing, editing, adding, and removing service requests
 * Allows the admin to manage service Requests
 * @author Jonathan Chang, imoralessirgo
 * @version iteration1
 */
public class UIControllerATVSR extends UIController {
    private static final String[] serviceRequestSetters  = {"", "", "", "setResolved", "setResolverID", ""};
    private static final String[] serviceRequestGetters  = {"getNodeID", "getServiceType", "getUserID", "isResolved", "getResolverID", "getMessage"};
    @FXML
    private ImageView backgroundImage;
    /**< The Various servicerequests Columns used for cell factories */

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
        List<ServiceRequest> serviceRequests = DBControllerSR.getServiceRequests(DBControllerSR.TYPE_ALL, conn);
        DBController.closeConnection(conn);
        serviceRequestTable.setItems(FXCollections.observableList(serviceRequests));
    }

}
