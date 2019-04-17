import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UIControllerVU extends UIController {

    private static final String[] userGetters  = {"getUserID", "getPermissions", "getUsername"};
    @FXML
    private ImageView backgroundImage;
    @FXML
    private TableView<User> usersTable; /**< The table that holds all of the users */

    /**
     * Called when the scene is first created
     * Sets up all the cell factories
     */
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        ObservableList<TableColumn<User, ?>> tableColumns = usersTable.getColumns();

        // Set up the uneditable columns
        for (int i = 0; i < 3; i++) {
            int indexOut = i;
            TableColumn<User, User> column = (TableColumn<User, User>) tableColumns.get(i);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            column.setCellFactory(param -> new TableCell<User, User>() {
                private Label label = new Label("TEST");
                private int index = indexOut;


                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);

                    runStringGetter(user, userGetters[index], label);
                    setGraphic(label);
                }
            });
        }
        // Initialize cell factories of the remove rsv column
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
                            try {
                                Connection conn = DBControllerAPI.dbConnect();
                                DBControllerAPI.deleteUser(user.getUserID(), conn);
                                conn.close();
                            }catch(SQLException e1){
                                e1.printStackTrace();
                            }
                            getTableView().getItems().remove(user);
                        }
                );
            }

        });
    }

    /**
     * Run when the scene is shown
     */
    @Override
    public void onShow() {
        Connection conn = DBControllerAPI.dbConnect();
        ObservableList<User> users = FXCollections.observableArrayList();
        try{
            ResultSet rs = conn.createStatement().executeQuery("Select * from USERS");
            while (rs.next()){
                users.add(new User(rs.getString(1),rs.getInt(2),rs.getString(3),rs.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < users.size(); i ++) {
            System.out.println(users.get(i));
        }
        usersTable.setItems(users);
    }

}
