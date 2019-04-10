package reservations;

import application.CurrentUser;
import application.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.sun.org.apache.regexp.internal.RESyntaxException;
import entities.Reservation;
import entities.ServiceRequest;
import entities.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.lang.reflect.Method;
import java.security.BasicPermission;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The UIController for the viewing and editing, a user's reservations
 * Allows the user to see and manage reservations
 * @author Ryan O'Brien
 * @version iteration2
 */
public class UIControllerRVVE extends UIController {
    private static final int[] lengthRequirements = {-1, 10, 10, 10, 8, 8};
    private static final String[] reservationSetters  = {"setNodeID", "setUserID", "setDate", "setStartTime", "setEndTime", ""};
    private static final String[] reservationGetters  = {"getRsvID", "getNodeID", "getUserID", "getDate", "getStartTime", "getEndTime"};
                                                    /**< The Various Reservation Columns used for cell factories */
    @FXML
    private MenuItem backButton; /**< The Back Button */

    @FXML
    private Menu homeButton; /**< The Home Button */

    @FXML
    private TableView<Reservation> reservationTable; /**< The table that holds all of the reservations */

    /**
     * Called when the scene is first created
     * Sets up all the cell factories
     */
    @FXML
    public void initialize() {
        List<TableColumn<Reservation, ?>> tableColumns = reservationTable.getColumns();
        // Initialize the cell factories of the reservation field columns

        TableColumn<Reservation, Reservation> rsvIDColumn = (TableColumn<Reservation, Reservation>) tableColumns.get(0);
        rsvIDColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        rsvIDColumn.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
            private Label label = new Label();
            private int index = 0; //RSVID is last field in Reservation

            @Override
            protected  void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                runStringGetter(reservation, reservationGetters[index], label);
                setGraphic(label);
            }
        });

        for(int i = 1; i < tableColumns.size() - 1; i++) {
            int indexOut = i;
            TableColumn<Reservation, Reservation> column = (TableColumn<Reservation, Reservation>) tableColumns.get(i);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            column.setCellFactory(param -> new EditableTextCell<Reservation, Reservation
                    >(column, indexOut) {

                // When the Reservation is updated on the textfield
                @Override
                protected void updateItem(Reservation reservation, boolean empty) {
                    super.updateItem(reservation, empty);

                    // Get the starting text of the label and textField
                    runStringGetterEditable(reservation, reservationGetters[index], label, textField);


                    // When an edit is committed with enter
                    textField.setOnAction(et -> {

                        // See if input is a valid date
                        if(index ==3) {
                            try {
                                //Date date = new SimpleDateFormat("yyyy-MM-dd").parse(textField.getText());
                                runSetter(reservation, reservationSetters[index], String.class, textField.getText());
                            } catch(Exception e) {
                                setGraphic(label);
                                textField.setText(label.getText());
                                return;
                            }
                        }

                        // See if input is a valid time
                        if(index == 4 | index == 5) {
                            try {
                                Time time = Time.valueOf(textField.getText());
                                runSetter(reservation, reservationSetters[index], String.class, textField.getText());
                            } catch(Exception e) {
                                setGraphic(label);
                                textField.setText(label.getText());
                                return;
                            }
                        }

                        try{
                            Connection conn = DBController.dbConnect();
                            System.out.println(reservation.getRsvID());
                            DBController.updateReservation(reservation, conn);
                            conn.close();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                        setGraphic(label);
                        label.setText(textField.getText());
                    });
                }
            });
        }
        // Initialize cell factories of the remove rsv column
        TableColumn<Reservation, Reservation> removeColumn = (TableColumn<Reservation, Reservation>) tableColumns.get(tableColumns.size() - 1);
        removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        removeColumn.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
            private JFXButton removeButton = new JFXButton("Cancel");

            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if(reservation == null) {
                    return;
                }
                setGraphic(removeButton);
                removeButton.setOnAction( e -> {
                            try {
                                Connection conn = DBController.dbConnect();
                                DBController.deleteReservation(reservation.getRsvID(), conn);
                                conn.close();
                            }catch(SQLException e1){
                                e1.printStackTrace();
                            }
                            getTableView().getItems().remove(reservation);
                        }
                );
            }

        });

    }

    /**
     * Run when the scene is shown
     * Gets the nodes from the database and puts them into the table
     */
    @Override
    public void onShow() {
        //DB get Nodes
        Connection conn = DBController.dbConnect();
        ObservableList<Reservation> rsvData = FXCollections.observableArrayList();
        if(CurrentUser.user.getPermissions() == User.BASIC_PERMISSIONS) {
            try {
                ResultSet rs = conn.createStatement().executeQuery("Select * from RESERVATIONS WHERE USERID = '" + CurrentUser.user.getUserID() + "'");
                while (rs.next()) {
                    rsvData.add(new Reservation(rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getString(6), rs.getInt(1)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if (CurrentUser.user.getPermissions() == User.ADMIN_PERMISSIONS) {
            try {
                ResultSet rs = conn.createStatement().executeQuery("Select * from RESERVATIONS");
                while (rs.next()) {
                    rsvData.add(new Reservation(rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getString(6), rs.getInt(1)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        reservationTable.setItems(rsvData);
    }

    /**
     * Goes back to the admin tools application menu
     */
    @FXML
    private void setBackButton() {
        this.goToScene(UIController.RESERVATIONS_MAIN_MENU);
    }


    @FXML
    private void setHomeButton() { this.goToScene(UIController.USER_MAIN_MENU_MAIN); }

}
