package helper;

import application.CurrentUser;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import database.DBController;
import database.DBControllerSR;
import entities.ServiceRequest;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pathfinding.UIControllerPUD;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public class ServiceRequestTableHelper extends UIController {
    private static final String[] serviceRequestSetters  = {"", "", "", "setResolved", "setResolverID", ""};
    private static final String[] serviceRequestGetters  = {"getNodeID", "getServiceType", "getUserID", "isResolved", "getResolverID", "getMessage"};

    private TableView<ServiceRequest> serviceRequestTable; /**< The table that holds all of the nodes */
    private TableColumn<ServiceRequest, ServiceRequest> nodeIDColumn;
    private TableColumn<ServiceRequest, ServiceRequest> serviceTypeColumn;
    private TableColumn<ServiceRequest, ServiceRequest> userIDColumn;
    private TableColumn<ServiceRequest, ServiceRequest> resolvedColumn;
    private TableColumn<ServiceRequest, ServiceRequest> resolverIDColumn;
    private TableColumn<ServiceRequest, ServiceRequest> messageColumn;
    private TableColumn<ServiceRequest, ServiceRequest> removeColumn;

    public ServiceRequestTableHelper(TableView<ServiceRequest> serviceRequestTable, TableColumn<ServiceRequest, ServiceRequest> nodeIDColumn, TableColumn<ServiceRequest, ServiceRequest> serviceTypeColumn, TableColumn<ServiceRequest, ServiceRequest> userIDColumn, TableColumn<ServiceRequest, ServiceRequest> resolvedColumn, TableColumn<ServiceRequest, ServiceRequest> resolverIDColumn, TableColumn<ServiceRequest, ServiceRequest> messageColumn, TableColumn<ServiceRequest, ServiceRequest> removeColumn) {
        this.serviceRequestTable = serviceRequestTable;
        this.nodeIDColumn = nodeIDColumn;
        this.serviceTypeColumn = serviceTypeColumn;
        this.userIDColumn = userIDColumn;
        this.resolvedColumn = resolvedColumn;
        this.resolverIDColumn = resolverIDColumn;
        this.messageColumn = messageColumn;
        this.removeColumn = removeColumn;
        setupColumns();
    }

    private void setupColumns() {
        // Set up the uneditable columns
        List<TableColumn<ServiceRequest, ServiceRequest>> tableColumns = new LinkedList<TableColumn<ServiceRequest, ServiceRequest>>();
        tableColumns.add(nodeIDColumn);
        tableColumns.add(serviceTypeColumn);
        tableColumns.add(userIDColumn);
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
                    if(checkBox.isSelected()) {
                        serviceRequest.setResolverID(CurrentUser.user.getUserID());
                    } else {
                        serviceRequest.setResolverID(null);
                    }
                    serviceRequestTable.refresh();
                    Connection conn = DBController.dbConnect();
                    DBControllerSR.updateServiceRequest(serviceRequest, conn);
                    if (serviceRequest.getServiceType().equals("Flower Delivery")) {
                        String phoneNum = serviceRequest.getMessage().substring(0, 10);
                        Twilio.init(UIControllerPUD.ACCOUNT_SID, UIControllerPUD.AUTH_TOKEN);
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
        resolverIDColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        resolverIDColumn.setCellFactory(param -> new UIController.EditableTextCell<ServiceRequest, ServiceRequest>(resolverIDColumn, 4) {
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
        messageColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        messageColumn.setCellFactory(param -> new TableCell<ServiceRequest, ServiceRequest>() {
            private Label label = new Label("TEST");
            private int index = 5;

            @Override
            protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
                super.updateItem(serviceRequest, empty);

                runStringGetter(serviceRequest, serviceRequestGetters[index], label);

                setGraphic(label);
            }
        });

        // Initialize cell factories of the remove service request column
        if(CurrentUser.user.getPermissions() != 3){
            removeColumn.setVisible(false);
        } else {
            removeColumn.setVisible(true);
            removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            removeColumn.setCellFactory(param -> new TableCell<ServiceRequest, ServiceRequest>() {
                private JFXButton removeButton = new JFXButton("Remove");

                @Override
                protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
                    super.updateItem(serviceRequest, empty);
                    if (serviceRequest == null) {
                        return;
                    }
                    setGraphic(removeButton);
                    removeButton.setOnAction(e -> {
                        serviceRequestTable.getItems().remove(serviceRequest);
                        Connection conn = DBController.dbConnect();
                        DBControllerSR.deleteServiceRequest(serviceRequest.getServiceID(), conn);
                        DBController.closeConnection(conn);
                        serviceRequestTable.refresh();
                    });
                }
            });
        }
    }
}
