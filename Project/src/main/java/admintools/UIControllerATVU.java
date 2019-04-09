package admintools;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import entities.User;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;

/**
 * The UIController for viewing and editing users
 * @author Jonathan Chang
 * @version iteration2
 */
public class UIControllerATVU extends UIController {
    private static final int[] lengthRequirements = {10, 15, 15, -1};
    private static final String[] userGetters= {"getUserID", "getUsername", "NO", "getPermissions"};
    private static final String[] userSetters= {"setUserID", "setUsername", "setPassword", "setPermissions"};

    private static final List<String> userPermissionNames = new LinkedList<String>() {{add("User"); add("Administrator");}};
    private static final int[] userPermissions = {2, 3};

    @FXML
    private TableView<User> userTableView;

    @FXML
    private JFXButton addButton;

    /**
     * Initalizes all of the table column factories
     */
    @FXML
    public void initialize() {
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
                            setGraphic(label);
                            textField.setText(label.getText());
                            return;
                        }
                        runSetter(user, userSetters[index],String.class, textField.getText());
                        if(index == 0) {
                            // Remove User
                        }
                        // Update User
                        setGraphic(label);
                        label.setText(textField.getText());
                    });
                }
            });
        }

        // Initialize the permissions column
        TableColumn<User, User> passwordColumn = (TableColumn<User, User>) tableColumns.get(2);
        passwordColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        passwordColumn.setCellFactory(param -> new TableCell<User, User>() {
            private Label label = new Label("Password");
            private TextField textField = new TextField("TEST");

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if(user == null) {
                    return;
                }
                // Expand elements to the entire cell width
                textField.prefWidthProperty().bind(passwordColumn.prefWidthProperty());
                label.prefWidthProperty().bind(passwordColumn.prefWidthProperty());

                textField.setOnAction(et -> {
                    if(textField.getText().length() > lengthRequirements[2]) {
                        setGraphic(label);
                        textField.setText(label.getText());
                        return;
                    }

                    runSetter(user, userSetters[2],String.class, textField.getText());
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

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if(user == null) {
                    return;
                }
                choiceBox.setItems(FXCollections.observableList(userPermissionNames));
                setGraphic(choiceBox);

                choiceBox.setOnAction(et -> {
                    String permissionName = choiceBox.getValue();
                    int permission = userPermissions[userPermissionNames.indexOf(permissionName)];
                    // DB Update User
                });
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
                    //DB Remove User
                });
            }
        });
    }

    @Override
    public void onShow() {
        //DB Get Items
        //userTableView.setItems(userList);
    }

    @FXML
    private void setAddButton() {
        User user = new User("", "", "", 1);
        userTableView.getItems().add(user);
    }
}
