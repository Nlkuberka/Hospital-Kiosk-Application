package reservations;

import application.CurrentUser;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import database.DBControllerRW;
import entities.Reservation;
import entities.User;
import helper.ReservationTableHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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

    @FXML
    private ImageView backgroundImage;

    /**
     * Called when the scene is first created
     * Sets up all the cell factories
     */
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
        List<TableColumn<Reservation, ?>> tableColumns = reservationTable.getColumns();
        // Initialize the cell factories of the reservation field columns
        new ReservationTableHelper(reservationTable,
                (TableColumn<Reservation, Reservation>) tableColumns.get(0),
                (TableColumn<Reservation, Reservation>) tableColumns.get(1),
                (TableColumn<Reservation, Reservation>) tableColumns.get(2),
                (TableColumn<Reservation, Reservation>) tableColumns.get(3),
                (TableColumn<Reservation, Reservation>) tableColumns.get(4),
                (TableColumn<Reservation, Reservation>)  tableColumns.get(5),
                (TableColumn<Reservation, Reservation>) tableColumns.get(6)
        );
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


}
