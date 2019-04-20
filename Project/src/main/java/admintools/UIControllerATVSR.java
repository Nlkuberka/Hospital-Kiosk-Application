package admintools;

import application.CurrentUser;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import database.DBController;
import application.UIController;
import database.DBControllerSR;
import entities.ServiceRequest;

import com.jfoenix.controls.JFXCheckBox;

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

import static application.UIControllerPUD.ACCOUNT_SID;
import static application.UIControllerPUD.AUTH_TOKEN;

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

        // Set up the uneditable columns
        for(int i = 0; i < 3; i++) {
            int indexOut = i;
            TableColumn<ServiceRequest, ServiceRequest> column = (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(i);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            column.setCellFactory(param -> new TableCell<ServiceRequest, ServiceRequest>() {
                private Label label = new Label("TEST");
                private int index = indexOut;


                @Override
                protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
                    super.updateItem(serviceRequest, empty);

                    runStringGetter(serviceRequest, serviceRequestGetters[index], label);
                    setGraphic(label);
                }
            });
        }
        // Set up the Resolved Column
        TableColumn<ServiceRequest, ServiceRequest> resolvedColumn = (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(3);
        resolvedColumn.setStyle( "-fx-alignment: CENTER;");
        resolvedColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        resolvedColumn.setCellFactory(param -> new TableCell<ServiceRequest, ServiceRequest>() {
            private JFXCheckBox checkBox = new JFXCheckBox();
            private int index = 3;

            @Override
            protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
                super.updateItem(serviceRequest, empty);
                if(serviceRequest == null) {
                    return;
                }
                // Get the initial value of the checkbox
                try {
                    Method method = serviceRequest.getClass().getMethod(serviceRequestGetters[index]);
                    checkBox.setSelected((boolean) method.invoke(serviceRequest));
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                setGraphic(checkBox);
                checkBox.setOnAction(et -> {
                    runSetter(serviceRequest, serviceRequestSetters[index], boolean.class, checkBox.isSelected());
                    serviceRequest.setResolverID(CurrentUser.user.getUserID());
                    serviceRequestTable.refresh();
                    Connection conn = DBController.dbConnect();
                    DBControllerSR.updateServiceRequest(serviceRequest,conn);
                    if(serviceRequest.getServiceType().equals("Flower Delivery")){
                        String phoneNum = serviceRequest.getMessage().substring(0,10);
                        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                        Message message = Message.creator(
                                new com.twilio.type.PhoneNumber("+1" + phoneNum),
                                new com.twilio.type.PhoneNumber("+17472290044"),
                                "Flowers have been Delivered")
                                .create();
                        System.out.println("It did shit");
                    }
                    DBController.closeConnection(conn);
                });
            }

        });
        // Set up the ResolverID Column
        TableColumn<ServiceRequest, ServiceRequest> resolverIDColumn = (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(4);
        resolverIDColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        resolverIDColumn.setCellFactory(param -> new EditableTextCell<ServiceRequest, ServiceRequest>(resolverIDColumn, 4) {
            @Override
            protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
                super.updateItem(serviceRequest, empty);

                runStringGetterEditable(serviceRequest, serviceRequestGetters[index], label, textField);

                textField.setOnAction(et -> {
                    // Check Length
                    if(textField.getText().length() > 10) {
                        setGraphic(label);
                        textField.setText(label.getText());
                        popupMessage("Field must have equal to or less than " +  10 + " characters.", true);
                        return;
                    }
                    runSetter(serviceRequest, serviceRequestSetters[index],String.class, textField.getText());
                    Connection conn = DBController.dbConnect();
                    DBControllerSR.updateServiceRequest(serviceRequest,conn);
                    DBController.closeConnection(conn);
                    setGraphic(label);
                    label.setText(textField.getText());
                });
            }
        });

        // Setup the message column
        TableColumn<ServiceRequest, ServiceRequest> column = (TableColumn<ServiceRequest, ServiceRequest>) tableColumns.get(5);
        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        column.setCellFactory(param -> new TableCell<ServiceRequest, ServiceRequest>() {
            private Label label = new Label("TEST");
            private int index = 5;

            @Override
            protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
                super.updateItem(serviceRequest, empty);

                runStringGetter(serviceRequest, serviceRequestGetters[index], label);

                setGraphic(label);
            }
        });
    }

    /**
     * Run when the scene is shown
     */
    @Override
    public void onShow() {
        Connection conn = DBController.dbConnect();
        ObservableList<ServiceRequest> serviceRequests = FXCollections.observableArrayList();
        try{
            ResultSet rs = conn.createStatement().executeQuery("Select * from SERVICEREQUEST");
            while (rs.next()){
                serviceRequests.add(new ServiceRequest(rs.getString(2),rs.getString(3),rs.getString(4),
                        rs.getString(5),rs.getBoolean(6),rs.getString(7),rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < serviceRequests.size(); i ++) {
            System.out.println(serviceRequests.get(i));
        }
        serviceRequestTable.setItems(serviceRequests);
    }

    /**
     * Goes back to the admin tools application menu
     */
    @FXML
    private void setBackButton() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }
}
