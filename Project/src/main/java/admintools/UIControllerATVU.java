package admintools;

import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import database.DBControllerU;
import entities.User;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The UIController for viewing and editing users
 * @author Jonathan Chang
 * @version iteration2
 */
public class UIControllerATVU extends UIController {
    private static final int[] lengthRequirements = {10, 15, 15, -1};
    private static final String[] userGetters = {"getUserID", "getUsername", "NO", "getPermissions"};
    private static final String[] userSetters = {"setUserID", "setUsername", "setPassword", "setPermissions"};

    private static final List<String> userPermissionNames = new LinkedList<String>() {{add("Guest"); add("User"); add("Administrator");}};
    private static final List<Integer> userPermissions = new LinkedList<Integer>(){{add(1); add(2); add(3);}};
    @FXML
    private ImageView backgroundImage;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private JFXButton addButton;

    /**
     * Initalizes all of the table column factories
     */
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        List<TableColumn<User, ?>> tableColumns = userTableView.getColumns();
        // Initialize the UserID and username columns
        for(int i = 0; i < 2; i++) {
            int indexOut = i;
            TableColumn<User, User> column = (TableColumn<User, User>) tableColumns.get(i);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            column.setCellFactory(param -> new EditableTextCell<User, User>(column, indexOut) {

                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);

                    runStringGetter(user, userGetters[index], label);

                    textField.setOnAction(et -> {
                        // Check Length
                        if(textField.getText().length() > lengthRequirements[index]) {
                            popupMessage("Field must be less than " + lengthRequirements[index] + " characters.", true);
                            setGraphic(label);
                            textField.setText(label.getText());
                            return;
                        }
                        runSetter(user, userSetters[index],String.class, textField.getText());

                        Connection conn = DBController.dbConnect();
                        if(index == 0) {
                            DBController.createTable("Delete From USERS WHERE USERID = '"+label.getText()+"'",conn);
                            DBControllerU.addUser(user, conn);
                        } else {
                            DBControllerU.updateUser(user.getUserID(), user, conn);
                        }

                        DBController.closeConnection(conn);

                        setGraphic(label);
                        label.setText(textField.getText());
                    });
                }
            });
        }

        // Initialize the password column
        TableColumn<User, User> passwordColumn = (TableColumn<User, User>) tableColumns.get(2);
        passwordColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        // Code for a textfield that is editable but the user never sees the actual value
        passwordColumn.setCellFactory(param -> new TableCell<User, User>() {
            private Label label = new Label("Password");
            private TextField textField = new TextField("TEST");

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if(user == null) {
                    return;
                }
                setGraphic(label);
                // Expand elements to the entire cell width
                textField.prefWidthProperty().bind(passwordColumn.prefWidthProperty());
                label.prefWidthProperty().bind(passwordColumn.prefWidthProperty());

                textField.setOnAction(et -> {
                    if(textField.getText().length() > lengthRequirements[2]) {
                        popupMessage("Field must be less than " + lengthRequirements[2] + " characters.", true);

                        setGraphic(label);
                        textField.setText(label.getText());
                        return;
                    }

                    runSetter(user, userSetters[2],String.class, textField.getText());
                    Connection conn = DBController.dbConnect();
                    DBControllerU.updateUser(user.getUserID(), user, conn);
                    DBController.closeConnection(conn);
                    setGraphic(label);
                });

                textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(!newValue) {
                            setGraphic(label);
                        }
                    }
                });

                label.setOnMouseClicked(et -> {
                    if(et.getClickCount() == 2) {
                        setGraphic(textField);
                        textField.setText(label.getText());
                        textField.positionCaret(label.getText().length() - 1);
                    }
                });
            }
        });

        // Initialize the permissions column
        TableColumn<User, User> permissionsColumn = (TableColumn<User, User>) tableColumns.get(3);
        permissionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        permissionsColumn.setCellFactory(param -> new TableCell<User, User>() {
            private ChoiceBox<String> choiceBox = new ChoiceBox<>();
            private boolean first = true;

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if(user == null) {
                    return;
                }
                if(first) {
                    choiceBox.setItems(FXCollections.observableList(userPermissionNames));
                    choiceBox.getSelectionModel().select(userPermissionNames.get(userPermissions.indexOf(user.getPermissions())));
                    first = false;
                }

                choiceBox.setOnAction(et -> {
                    String permissionName = choiceBox.getSelectionModel().getSelectedItem();
                    if(permissionName == null) {
                        return;
                    }
                    int permission = userPermissions.get(userPermissionNames.indexOf(permissionName));
                    user.setPermissions(permission);

                    Connection conn = DBController.dbConnect();
                    DBControllerU.updateUser(user.getUserID(),user,conn);
                    DBController.closeConnection(conn);
                });
                setGraphic(choiceBox);
            }
        });

        // Initialize the service request column
        TableColumn<User, User> serviceRequestsColumn = (TableColumn<User, User>) tableColumns.get(4);
        serviceRequestsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        serviceRequestsColumn.setCellFactory(param -> new TableCell<User, User>() {
            private AnchorPane pane = new AnchorPane();
            private JFXButton addSRButton = new JFXButton("Add");
            private List<ChoiceBox<String>> choiceBoxes = new LinkedList<ChoiceBox<String>>();

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if(user == null) {
                    return;
                }

                setUpChoiceBoxes(user);
                pane.getChildren().add(addSRButton);
                setGraphic(pane);

                addSRButton.setOnAction(et -> {
                    setupChoiceBox(user, "");
                    pane.setPrefHeight(30.0 * choiceBoxes.size());
                    addSRButton.setLayoutY(30.0 * choiceBoxes.size());
                });
            }

            /**
             * Gets all of the current service requests that the user can fulfill
             * @return The list of services that the user can fulfill
             */
            private List<String> getAllServiceRequests() {
                List<String> result = new LinkedList<String>();
                for(int i = 0; i < choiceBoxes.size(); i++) {
                    result.add(choiceBoxes.get(i).getValue());
                }
                return result;
            }

            /**
             * Sets up all of the choice boxes
             * @param user The user to set up for
             */
            private void setUpChoiceBoxes(User user) {
                pane.getChildren().clear();
                choiceBoxes.clear();
                List<String> serviceRequests = user.getServiceRequestFullfillment();
                for(int i = 0; i < serviceRequests.size(); i++) {
                    setupChoiceBox(user, serviceRequests.get(i));
                }
                pane.setPrefHeight(30.0 * choiceBoxes.size());
                addSRButton.setLayoutY(30.0 * choiceBoxes.size());
            }

            /**
             * Sets up a single choice box for the user
             * @param user The user to set up for
             * @param choice The initial choice for the choicebox
             */
            private void setupChoiceBox(User user, String choice) {
                ChoiceBox<String> choiceBox = new ChoiceBox<String>();
                choiceBox.setItems(FXCollections.observableList(new LinkedList<String>(Arrays.asList(User.serviceRequests))));
                choiceBox.getItems().add("Remove");
                choiceBox.getSelectionModel().select(choice);
                choiceBox.setOnAction(et -> {
                    if(choiceBox.getValue() == "Remove" || isDuplicate(choiceBox)) {
                        choiceBoxes.remove(choiceBox);
                        user.setServiceRequestsFullfillment(getAllServiceRequests());
                        setUpChoiceBoxes(user);
                        pane.getChildren().add(addSRButton);
                    }
                    user.setServiceRequestsFullfillment(getAllServiceRequests());
                    Connection conn = DBController.dbConnect();
                    DBControllerU.updateUser(user.getUserID(),user,conn);
                    DBController.closeConnection(conn);
                });
                choiceBox.setMinWidth(225.0);
                choiceBox.setLayoutY(30.0 * choiceBoxes.size());
                choiceBoxes.add(choiceBox);
                pane.getChildren().add(choiceBox);
            }

            /**
             * Tests if the given choicebox is a dulicate of another
             * @param choiceBox The choice to test
             * @return Whether this choicebox is a duplicate or not
             */
            private boolean isDuplicate(ChoiceBox choiceBox) {
                for(int i = 0; i < choiceBoxes.size(); i++) {
                    if(choiceBoxes.indexOf(choiceBox) == i) {
                        continue;
                    }
                    if(choiceBoxes.get(i).getValue() == choiceBox.getValue()) {
                        return true;
                    }
                }
                return false;
            }
        });

        // Initialize cell factories of the remove node column
        TableColumn<User, User> removeColumn = (TableColumn<User, User>) tableColumns.get(tableColumns.size() - 1);
        removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        removeColumn.setCellFactory(param -> new TableCell<User, User>() {
            private JFXButton removeButton = new JFXButton("Remove");

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if(user == null) {
                    return;
                }
                setGraphic(removeButton);
                removeButton.setOnAction( e -> {
                    userTableView.getItems().remove(user);
                    Connection conn = DBController.dbConnect();
                    DBControllerU.deleteUser(user,conn);
                    DBController.closeConnection(conn);
                });
            }
        });
    }

    @Override
    public void onShow() {
        Connection conn = DBController.dbConnect();
        List<User> userList = DBControllerU.getUser(conn);
        DBController.closeConnection(conn);
        userTableView.setItems(FXCollections.observableList(userList));
    }

    @FXML
    private void setAddButton() {
        User user = new User("", "", "", 0);
        user.setPermissions(User.GUEST_PERMISSIONS);
        userTableView.getItems().add(user);
    }

}
